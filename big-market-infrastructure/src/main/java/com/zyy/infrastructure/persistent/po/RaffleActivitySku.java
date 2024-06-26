package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivitySku {

	/** 自增 ID **/
	private Integer id;
	/** sku **/
	private Long sku;
	/** 活动 ID **/
	private Long activityId;
	/** 活动个人参与次数 **/
	private Long activityCountId;
	/** 商品库存 **/
	private Integer stockCount;
	/** 剩余库存 **/
	private Integer stockCountSurplus;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
