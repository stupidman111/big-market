package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityOrder {

	/** 自增 ID **/
	private Long id;
	/** 用户 ID **/
	private String userId;
	/** sku **/
	private Long sku;
	/** 活动 ID **/
	private Long activityId;
	/** 活动名称 **/
	private String activityName;
	/** 抽奖策略 ID **/
	private Long strategyId;
	/** 订单 ID **/
	private String orderId;
	/** 下单时间 **/
	private Date orderTime;
	/** 总次数 **/
	private Integer totalCount;
	/** 月次数 **/
	private Integer monthCount;
	/** 日次数 **/
	private Integer dayCount;
	/** 订单状态 **/
	private String state;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
