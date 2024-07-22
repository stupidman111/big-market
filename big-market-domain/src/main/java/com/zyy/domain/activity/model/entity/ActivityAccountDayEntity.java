package com.zyy.domain.activity.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日活动账户
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAccountDayEntity {

	/** 用户 ID **/
	private String userId;
	/** 活动 ID **/
	private Long activityId;
	/** 日期 (yyyy-mm-dd) **/
	private String day;
	/** 日次数 **/
	private Integer dayCount;
	/** 日次数-剩余 **/
	private Integer dayCountSurplus;
}
