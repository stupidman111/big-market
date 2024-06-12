package com.zyy.domain.strategy.service.rule.chain.impl;

import com.zyy.domain.strategy.service.armory.IStrategyDispatch;
import com.zyy.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.zyy.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {

	@Resource
	protected IStrategyDispatch strategyDispatch;

	@Override
	public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
		Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
		log.info("抽奖责任链-默认处理 userId: {}, startegyId: {}, ruleModel: {}, awardId: {}",
				userId, strategyId, ruleModel(), awardId);

		return DefaultChainFactory.StrategyAwardVO.builder()
				.awardId(awardId)
				.logicModel(ruleModel())
				.build();
	}

	@Override
	protected String ruleModel() {
		return DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode();
	}


}
