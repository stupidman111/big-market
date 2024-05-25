package com.zyy.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;

public interface IStrategyArmory {

	boolean assembleLotteryStrategy(Long strategyId);

	Integer getRandomAwardId(Long strategyId);
}
