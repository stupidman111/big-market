package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityAccount {

	/** 自增 ID **/
	private Long id;
	/** 用户 ID **/
	private String userId;
	/** 活动 ID **/
	private Long activityId;
	/** 总次数 **/
	private Integer totalCount;
	/** 总次数-剩余 **/
	private Integer totalCountSurplus;
	/** 日次数 **/
	private Integer dayCount;
	/** 日次数-剩余 **/
	private Integer dayCountSurplus;
	/** 月次数 **/
	private Integer monthCount;
	/** 月次数-剩余 **/
	private Integer monthCountSurplus;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
