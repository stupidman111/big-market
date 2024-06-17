package com.zyy.domain.strategy.service.raffle;

import com.zyy.domain.strategy.model.valobj.RuleTreeVO;
import com.zyy.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.zyy.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.AbstractRaffleStrategy;
import com.zyy.domain.strategy.service.armory.IStrategyDispatch;
import com.zyy.domain.strategy.service.rule.chain.ILogicChain;
import com.zyy.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

import com.zyy.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.zyy.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

	public DefaultRaffleStrategy(IStrategyRepository repository,
								 IStrategyDispatch strategyDispatch,
								 DefaultChainFactory defaultChainFactory,
								 DefaultTreeFactory defaultTreeFactory) {
		super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
	}

	@Override
	public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
		ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
		return logicChain.logic(userId, strategyId);
	}

	@Override
	public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
		StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
		if (null == strategyAwardRuleModelVO) {//对应的奖品，查不到规则（无规则），直接返回
			return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();
		}
		RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
		if (null == ruleTreeVO) {
			throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 " + strategyAwardRuleModelVO.getRuleModels());
		}
		IDecisionTreeEngine treeEngine = defaultTreeFactory.openlogicTree(ruleTreeVO);
		return treeEngine.process(userId, strategyId, awardId);
	}

	@Override
	public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
		return repository.takeQueueValue();
	}

	@Override
	public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
		repository.updateStrategyAwardStock(strategyId, awardId);
	}
}
