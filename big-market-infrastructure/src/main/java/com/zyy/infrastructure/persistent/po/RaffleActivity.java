package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivity {

	/** 自增 ID **/
	private Long id;
	/** 活动 ID **/
	private Long activityId;
	/** 活动名称 **/
	private String activityName;
	/** 活动描述 **/
	private String activityDesc;
	/** 开始时间 **/
	private Date beginDateTime;
	/** 结束时间 **/
	private Date endDateTime;
	/** 库存总量 **/
	private Integer stockCount;
	/** 剩余库存 **/
	private Integer stockCountSurplus;
	/** 活动参与次数配置 **/
	private Long activityCountId;
	/** 抽奖策略 ID **/
	private Long strategyId;
	/** 活动状态 **/
	private String status;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
