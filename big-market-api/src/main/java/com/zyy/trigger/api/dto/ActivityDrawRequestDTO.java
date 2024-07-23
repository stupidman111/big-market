package com.zyy.trigger.api.dto;

import lombok.Data;

/**
 * 活动抽奖请求对象
 */
@Data
public class ActivityDrawRequestDTO {

	/** 用户 ID **/
	private String userId;

	/** 活动 ID **/
	private Long activityId;
}
