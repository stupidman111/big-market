package com.zyy.infrastructure.persistent.po;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 抽奖活动账户表-月次数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleActivityAccountMonth {
	/** 自增 ID **/
	private Integer id;
	/** 用户 ID **/
	private String userId;
	/** 活动 ID **/
	private Long activityId;
	/** 日期(yyyy-mm) **/
	private String month;
	/** 日次数 **/
	private Integer monthCount;
	/** 日次数-剩余 **/
	private Integer monthCountSurplus;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
