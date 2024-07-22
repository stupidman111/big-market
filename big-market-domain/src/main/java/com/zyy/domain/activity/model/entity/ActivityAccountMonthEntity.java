package com.zyy.domain.activity.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAccountMonthEntity {

	/** 用户 ID **/
	private String userId;
	/** 活动 ID **/
	private Long activityId;
	/** 月 (yyyy-mm) **/
	private String month;
	/** 月次数 **/
	private Integer monthCount;
	/** 月次数-剩余 **/
	private Integer monthCountSurplus;
}
