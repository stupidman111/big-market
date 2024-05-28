package com.zyy.domain.strategy.model.entity;

import lombok.Data;

@Data
public class RuleMatterEntity {

	/** 用户ID **/
	private String userId;
	/** 策略ID **/
	private Long strategyId;
	/** 抽奖奖品ID（规则类型为策略，则不需要奖品ID】**/
	private Integer awardId;
	/** 抽奖规则类型 **/
	private String ruleModel;
}
