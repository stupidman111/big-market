package com.zyy.domain.strategy.repository;


import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {

	//查询指定策略下所有【策略-奖品】对应信息
	List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);


	void storeStrategyAwardSearchRateTable(Long strategyId, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable);


	Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey);

	int getRateRange(Long strategyId);

}
