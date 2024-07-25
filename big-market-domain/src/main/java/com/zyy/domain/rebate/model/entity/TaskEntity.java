package com.zyy.domain.rebate.model.entity;

import com.zyy.domain.rebate.event.SendRebateMessageEvent;
import com.zyy.domain.rebate.model.valobj.TaskStateVO;
import com.zyy.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

	/** 活动 ID **/
	private String userId;
	/** 消息主题 **/
	private String topic;
	/** 消息编号 **/
	private String  messageId;
	/** 消息主体 **/
	private BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message;
	/** 任务状态 **/
	private TaskStateVO state;
}
