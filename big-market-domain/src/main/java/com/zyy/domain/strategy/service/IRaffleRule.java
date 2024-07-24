package com.zyy.domain.strategy.service;

import java.util.Map;

/**
 * 抽奖规则接口：提供对规则的业务功能查询
 */
public interface IRaffleRule {

	Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
