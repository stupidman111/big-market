package com.zyy.domain.rebate.event;

import com.zyy.types.event.BaseEvent;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SendRebateMessageEvent extends BaseEvent<SendRebateMessageEvent.RebateMessage> {

	@Value("send_rebate")
	private String topic;

	@Override
	public EventMessage<RebateMessage> buildEventMessage(RebateMessage data) {
		return EventMessage.<SendRebateMessageEvent.RebateMessage>builder()
				.id(RandomStringUtils.randomNumeric(11))
				.timestamp(new Date())
				.data(data)
				.build();
	}

	@Override
	public String topic() {
		return topic;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RebateMessage {
		/** 用户 ID **/
		private String userId;
		/** 返利描述 **/
		private String rebateDesc;
		/** 返利类型 **/
		private String rebateType;
		/** 返利配置 **/
		private String rebateConfig;
		/** 业务 ID -唯一 ID，确保幂等性 **/
		private String bizId;
	}
}
