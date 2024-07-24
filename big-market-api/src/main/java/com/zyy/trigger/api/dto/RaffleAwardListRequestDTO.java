package com.zyy.trigger.api.dto;


import lombok.Data;

/**
 * 请求 【抽奖奖品列表】 DTO
 */
@Data
public class RaffleAwardListRequestDTO {

	/** 用户 ID **/
	private String userId;
	/** 抽奖活动 ID **/
	private Long activityId;
}
