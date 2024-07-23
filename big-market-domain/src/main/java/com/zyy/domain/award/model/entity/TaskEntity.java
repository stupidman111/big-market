package com.zyy.domain.award.model.entity;

import com.zyy.domain.award.event.SendAwardMessageEvent;
import com.zyy.domain.award.model.valobj.TaskStateVO;
import com.zyy.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
	/** 用户 ID **/
	private String userId;
	/** 消息主题 **/
	private String topic;
	/** 消息编号 **/
	private String messageId;
	/** 消息主体 **/
	private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;
	/** 任务状态；create-创建、completed-完成、fail-失败 **/
	private TaskStateVO state;
}
