package com.zyy.domain.strategy.service.rule.tree.impl;

import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.zyy.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.zyy.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * （随机）幸运积分节点
 */
@Slf4j
@Component("rule_luck_award")
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {
	@Override
	public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardID) {
		return DefaultTreeFactory.TreeActionEntity.builder()
				.ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
				.strategyAwardData(DefaultTreeFactory.StrategyAwardVO.builder()
						.awardId(101)
						.awardRuleValue("1,100")
						.build())
				.build();
	}
}
