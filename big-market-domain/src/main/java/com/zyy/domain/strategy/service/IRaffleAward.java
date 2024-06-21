package com.zyy.domain.strategy.service;

import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

public interface IRaffleAward {

	List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
}
