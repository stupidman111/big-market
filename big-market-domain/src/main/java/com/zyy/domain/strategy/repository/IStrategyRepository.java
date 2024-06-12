package com.zyy.domain.strategy.repository;


import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.domain.strategy.model.entity.StrategyEntity;
import com.zyy.domain.strategy.model.entity.StrategyRuleEntity;
import com.zyy.domain.strategy.model.valobj.RuleTreeVO;
import com.zyy.domain.strategy.model.valobj.StrategyAwardRuleModelVO;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {

	//查询指定策略下所有【策略-奖品】对应信息
	List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);


	void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable);


	Integer getStrategyAwardAssemble(String key, Integer rateKey);

	int getRateRange(Long strategyId);

	int getRateRange(String key);

	//获取指定策略实体
	StrategyEntity queryStrategyEntityByStrategyById(Long strategyId);

	//获取指定【策略-规则】实体
	StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight);

	//查询指定strategy_rule的rule_value
	String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

	String queryStrategyRuleValue(Long strategyId, String ruleModel);


	StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

	RuleTreeVO queryRuleTreeVOByTreeId(String treeId);
}
