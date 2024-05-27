package com.zyy.domain.strategy.service.armory;

/**
 * 单一职责：只负责执行抽奖
 */
public interface IStrategyDispatch {

	Integer getRandomAwardId(Long strategyId);

	Integer getRandomAwardId(Long strategyId, String ruleWeightValue);
}
