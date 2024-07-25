package com.zyy.infrastructure.persistent.repository;

import com.alibaba.fastjson.JSON;
import com.zyy.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.zyy.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.zyy.domain.rebate.model.entity.TaskEntity;
import com.zyy.domain.rebate.model.valobj.BehaviorTypeVO;
import com.zyy.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import com.zyy.domain.rebate.repository.IBehaviorRebateRepository;
import com.zyy.infrastructure.event.EventPublisher;
import com.zyy.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import com.zyy.infrastructure.persistent.dao.ITaskDao;
import com.zyy.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import com.zyy.infrastructure.persistent.po.DailyBehaviorRebate;
import com.zyy.infrastructure.persistent.po.Task;
import com.zyy.infrastructure.persistent.po.UserBehaviorRebateOrder;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class BehaviorRebateRepository implements IBehaviorRebateRepository {

	@Resource
	private IDailyBehaviorRebateDao dailyBehaviorRebateDao;

	@Resource
	private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;

	@Resource
	private ITaskDao taskDao;

	@Resource
	private ApplicationContext applicationContext;

	@Resource
	private EventPublisher eventPublisher;

	@Override
	public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO) {
		//一种行为可能有多种返利方式
		List<DailyBehaviorRebate> dailyBehaviorRebates = dailyBehaviorRebateDao.queryDailyBehaviorRebateByBehaviorType(behaviorTypeVO.getCode());

		List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = new ArrayList<>(dailyBehaviorRebates.size());//值对象---不会改变的
		for (DailyBehaviorRebate dailyBehaviorRebate : dailyBehaviorRebates) {
			dailyBehaviorRebateVOS.add(DailyBehaviorRebateVO.builder()
					.behaviorType(dailyBehaviorRebate.getBehaviorType())
					.rebateDesc(dailyBehaviorRebate.getRebateDesc())
					.rebateType(dailyBehaviorRebate.getRebateType())
					.rebateConfig(dailyBehaviorRebate.getRebateConfig())
					.build());
		}

		return dailyBehaviorRebateVOS;
	}

	@Override
	public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates) {

		//在同一个事务中插入：用户行为返利订单、任务
			BehaviorRebateRepository self = applicationContext.getBean(BehaviorRebateRepository.class);

			for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
				//行为返利订单对象
				BehaviorRebateOrderEntity behaviorRebateOrderEntity = behaviorRebateAggregate.getBehaviorRebateOrderEntity();
				UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
				userBehaviorRebateOrder.setUserId(behaviorRebateOrderEntity.getUserId());
				userBehaviorRebateOrder.setOrderId(behaviorRebateOrderEntity.getOrderId());
				userBehaviorRebateOrder.setBehaviorType(behaviorRebateOrderEntity.getBehaviorType());
				userBehaviorRebateOrder.setRebateDesc(behaviorRebateOrderEntity.getRebateDesc());
				userBehaviorRebateOrder.setRebateType(behaviorRebateOrderEntity.getRebateType());
				userBehaviorRebateOrder.setRebateConfig(behaviorRebateOrderEntity.getRebateConfig());
				userBehaviorRebateOrder.setBizId(behaviorRebateOrderEntity.getBizId());

				//任务对象
				TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
				Task task = new Task();
				task.setUserId(taskEntity.getUserId());
				task.setTopic(taskEntity.getTopic());
				task.setMessageId(taskEntity.getMessageId());
				task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
				task.setState(taskEntity.getState().getCode());

				self.saveRecordAndTask(task, userBehaviorRebateOrder);
			}


		//同步发送 MQ 消息
		//可以改为异步的
		for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
			TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
			Task task = new Task();
			task.setUserId(taskEntity.getUserId());
			task.setMessageId(taskEntity.getMessageId());

			try {
				//发送消息
				eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
				//更新数据库记录（task表）
				taskDao.updateTaskSendMessageCompleted(task);
			} catch (Exception e) {
				//发送失败则由定时任务扫描库表重新发送
				log.error("写入返利记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
				taskDao.updateTaskSendMessageFail(task);
			}
		}

	}

	@Transactional
	public void saveRecordAndTask(Task task, UserBehaviorRebateOrder userBehaviorRebateOrder) {
		try {
			userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);
			taskDao.insert(task);
		} catch (DuplicateKeyException e) {
			log.error("写入返利记录，唯一索引冲突 userId: {}", userBehaviorRebateOrder.getUserId(), e);
			throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
		}
	}

}
