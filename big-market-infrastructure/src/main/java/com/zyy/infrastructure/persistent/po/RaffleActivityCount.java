package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityCount {

	/** 自增 ID **/
	private Long id;
	/** 活动次数编号 **/
	private Long activityCountId;
	/** 总次数 **/
	private Integer totalCount;
	/** 月次数 **/
	private Integer monthCount;
	/** 日次数 **/
	private Integer dayCount;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
