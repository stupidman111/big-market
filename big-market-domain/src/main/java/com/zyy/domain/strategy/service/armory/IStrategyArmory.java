package com.zyy.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;

/**
 * 单一职责：只负责装配抽奖池
 */
public interface IStrategyArmory {

	boolean assembleLotteryStrategy(Long strategyId);
}
