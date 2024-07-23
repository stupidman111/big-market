package com.zyy.domain.award.event;

import com.zyy.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 用户奖品记录事件消息
 */
@Component
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessageEvent.SendAwardMessage> {

	@Value("send_award")
	private String topic;

	@Override
	public EventMessage<SendAwardMessage> buildEventMessage(SendAwardMessage data) {
		return EventMessage.<SendAwardMessage>builder()
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
	public static class SendAwardMessage {
		/** 用户 ID **/
		private String userId;
		/** 奖品 ID **/
		private Integer awardId;
		/** 奖品标题 **/
		private String awardTitle;
	}
}
