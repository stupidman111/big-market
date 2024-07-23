package com.zyy.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 抽奖活动账户表-日次数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleActivityAccountDay {

	private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

	/** 自增 ID **/
	private Integer id;
	/** 用户 ID **/
	private String userId;
	/** 活动 ID **/
	private Long activityId;
	/** 日期(yyyy-mm-dd) **/
	private String day;
	/** 日次数 **/
	private Integer dayCount;
	/** 日次数-剩余 **/
	private Integer dayCountSurplus;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;

	public String currentDay() {

		return dateFormatDay.format(new Date());
	}
}
