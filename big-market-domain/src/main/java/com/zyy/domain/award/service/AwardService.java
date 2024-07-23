package com.zyy.domain.award.service;

import com.zyy.domain.award.event.SendAwardMessageEvent;
import com.zyy.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.zyy.domain.award.model.entity.TaskEntity;
import com.zyy.domain.award.model.entity.UserAwardRecordEntity;
import com.zyy.domain.award.model.valobj.TaskStateVO;
import com.zyy.domain.award.repository.IAwardRepository;
import com.zyy.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AwardService implements IAwardService {

	@Resource
	private IAwardRepository awardRepository;

	@Resource
	private SendAwardMessageEvent sendAwardMessageEvent;

	@Override
	public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {

		//构建消息
		SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
		sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
		sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
		sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());

		BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage
				= sendAwardMessageEvent.buildEventMessage(sendAwardMessage);


		//构建任务对象
		TaskEntity taskEntity = TaskEntity.builder()
				.userId(userAwardRecordEntity.getUserId())
				.topic(sendAwardMessageEvent.topic())
				.messageId(sendAwardMessageEventMessage.getId())
				.message(sendAwardMessageEventMessage)
				.state(TaskStateVO.create)
				.build();

		//构建聚合对象
		UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
				.userAwardRecordEntity(userAwardRecordEntity)
				.taskEntity(taskEntity)
				.build();

		//存储聚合对象--聚合对象落库需要在同一个事务下
		awardRepository.saveUserAwardRecord(userAwardRecordAggregate);
	}
}
