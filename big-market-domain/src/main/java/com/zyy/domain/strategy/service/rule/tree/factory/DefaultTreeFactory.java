package com.zyy.domain.strategy.service.rule.tree.factory;

import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.zyy.domain.strategy.model.valobj.RuleTreeVO;
import com.zyy.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.zyy.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.zyy.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultTreeFactory {
	private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

	public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeGroup) {
		this.logicTreeNodeGroup = logicTreeNodeGroup;
	}

	public IDecisionTreeEngine openlogicTree(RuleTreeVO ruleTreeVo) {
		return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVo);
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TreeActionEntity {
		private RuleLogicCheckTypeVO ruleLogicCheckType;
		private StrategyAwardData strategyAwardData;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class StrategyAwardData {
		/** 抽奖奖品ID **/
		private Integer awardId;
		/** 抽奖奖品规则 **/
		private String awardRuleValue;
	}
}
