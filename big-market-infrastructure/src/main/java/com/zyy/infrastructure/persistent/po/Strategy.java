package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class Strategy {

	/** 自增ID **/
	private Long id;
	/** 抽奖策略ID **/
	private Long strategyId;
	/** 抽奖策略描述信息 **/
	private String strategyDesc;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
