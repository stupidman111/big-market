package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class StrategyRule {

	/** 自增ID **/
	private Long id;
	/** 抽奖策略ID **/
	private Long strategyId;
	/** 奖品ID **/
	private Integer awardId;
	/** 抽奖规则类型 【1:策略规则, 2:奖品规则】**/
	private Integer ruleType;
	/** 抽奖规则模型 **/
	private String ruleModel;
	/** 抽奖规则比值 **/
	private String ruleValue;
	/** 抽奖规则描述 **/
	private String ruleDesc;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
