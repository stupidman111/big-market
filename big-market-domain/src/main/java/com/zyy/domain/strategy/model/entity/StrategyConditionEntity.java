package com.zyy.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyConditionEntity {

	/** 用户ID **/
	private String userId;
	/** 抽奖策略ID **/
	private Integer strategyId;
}
