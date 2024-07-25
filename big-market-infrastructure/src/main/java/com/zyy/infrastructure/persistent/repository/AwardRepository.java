package com.zyy.infrastructure.persistent.repository;

import com.alibaba.fastjson.JSON;
import com.zyy.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.zyy.domain.award.model.entity.TaskEntity;
import com.zyy.domain.award.model.entity.UserAwardRecordEntity;
import com.zyy.domain.award.repository.IAwardRepository;
import com.zyy.infrastructure.event.EventPublisher;
import com.zyy.infrastructure.persistent.dao.ITaskDao;
import com.zyy.infrastructure.persistent.dao.IUserAwardRecordDao;
import com.zyy.infrastructure.persistent.po.Task;
import com.zyy.infrastructure.persistent.po.UserAwardRecord;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Repository
@Slf4j
public class AwardRepository implements IAwardRepository {

	@Resource
	private IUserAwardRecordDao userAwardRecordDao;

	@Resource
	private ITaskDao taskDao;

	@Resource
	private EventPublisher eventPublisher;

	@Resource
	private ApplicationContext applicationContext;

	@Override
	public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
		UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
		TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
		String userId = userAwardRecordEntity.getUserId();
		Long activityId = userAwardRecordEntity.getActivityId();
		Integer awardId = userAwardRecordEntity.getAwardId();

		UserAwardRecord userAwardRecord = new UserAwardRecord();
		userAwardRecord.setUserId(userAwardRecordEntity.getUserId());
		userAwardRecord.setActivityId(userAwardRecordEntity.getActivityId());
		userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
		userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
		userAwardRecord.setAwardId(userAwardRecordEntity.getAwardId());
		userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
		userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
		userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

		Task task = new Task();
		task.setUserId(taskEntity.getUserId());
		task.setTopic(taskEntity.getTopic());
		task.setMessageId(taskEntity.getMessageId());
		task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
		task.setState(taskEntity.getState().getCode());

		try {
			AwardRepository self = applicationContext.getBean(AwardRepository.class);
			self.saveRecordAndTask(userAwardRecord, task);
		} catch (DuplicateKeyException e) {
			log.error("写入中奖记录，唯一索引冲突 userId: {} activityId: {} awardId: {}", userId, activityId, awardId, e);
			throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
		}

		handleTaskMessaging(task);
	}

	@Transactional
	public void saveRecordAndTask(UserAwardRecord userAwardRecord, Task task) {
		// 写入记录
		userAwardRecordDao.insert(userAwardRecord);
		// 写入任务
		taskDao.insert(task);
	}

	private void handleTaskMessaging(Task task) {
		try {
			// 发送消息【在事务外执行，如果失败还有任务补偿】
			eventPublisher.publish(task.getTopic(), task.getMessage());
			// 更新数据库记录，task 任务表
			taskDao.updateTaskSendMessageCompleted(task);
		} catch (Exception e) {
			log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", task.getUserId(), task.getTopic());
			taskDao.updateTaskSendMessageFail(task);
		}
	}
}
