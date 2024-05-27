package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyRuleDao {

	/** 查询所有【策略-规则】信息 **/
	List<StrategyRule> queryStrategyRuleList();

	StrategyRule queryStrategyRule(StrategyRule strategyRule);
}
