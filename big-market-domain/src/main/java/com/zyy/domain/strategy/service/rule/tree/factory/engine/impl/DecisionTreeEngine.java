package com.zyy.domain.strategy.service.rule.tree.factory.engine.impl;

import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.zyy.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import com.zyy.domain.strategy.model.valobj.RuleTreeNodeVO;
import com.zyy.domain.strategy.model.valobj.RuleTreeVO;
import com.zyy.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.zyy.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.zyy.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {

	private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

	private final RuleTreeVO ruleTreeVO;

	public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
		this.logicTreeNodeGroup = logicTreeNodeGroup;
		this.ruleTreeVO = ruleTreeVO;
	}

	@Override
	public DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId, Date endDateTime) {

		DefaultTreeFactory.StrategyAwardVO strategyAwardData = null;

		//获取树根节点、节点集合
		String curNode = ruleTreeVO.getTreeRootRuleNode();
		Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

		//遍历节点
		RuleTreeNodeVO ruleTreeNode = treeNodeMap.get(curNode);
		while (null != curNode) {
			ILogicTreeNode logicTreeNoode = logicTreeNodeGroup.get(ruleTreeNode.getRuleKey());
			String ruleValue = ruleTreeNode.getRuleValue();

			DefaultTreeFactory.TreeActionEntity logicEntity = logicTreeNoode.logic(userId, strategyId, awardId, ruleValue, endDateTime);
			RuleLogicCheckTypeVO ruleLogicCheckType = logicEntity.getRuleLogicCheckType();
			strategyAwardData = logicEntity.getStrategyAwardVO();

			log.info("决策树引擎【{}】 treeId: {}, node: {}, code: {}",
					ruleTreeVO.getTreeName(),
					ruleTreeVO.getTreeId(),
					curNode,
					ruleLogicCheckType.getCode());

			//获取下一个节点
			curNode = nextNode(ruleLogicCheckType.getCode(), ruleTreeNode.getTreeNodeLineVOList());
			ruleTreeNode = treeNodeMap.get(curNode);

		}

		//返回信息
		return strategyAwardData;
	}

	public String nextNode(String matterValue, List<RuleTreeNodeLineVO> treeNodeLineVOList) {
		if (null == treeNodeLineVOList || treeNodeLineVOList.isEmpty()) return null;
		for (RuleTreeNodeLineVO nodeLine : treeNodeLineVOList) {
			if (decisionLogic(matterValue, nodeLine)) {
				return nodeLine.getRuleNodeTo();
			}
		}
		return null;
 	}

	 public boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine) {
		 switch (nodeLine.getRuleLimitType()) {
			 case EQUAL:
				 return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
			 // 以下规则暂时不需要实现
			 case GT:
			 case LT:
			 case GE:
			 case LE:
			 default:
				 return false;
		}
	 }
}
