package com.zyy.domain.strategy.service.rule.chain.impl;

import com.zyy.domain.strategy.service.armory.IStrategyDispatch;
import com.zyy.domain.strategy.service.rule.chain.AbstractLogicChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {

	@Resource
	protected IStrategyDispatch strategyDispatch;

	@Override
	public Integer logic(String userId, Long strategyId) {
		Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
		log.info("抽奖责任链-默认处理 userId: {}, startegyId: {}, ruleModel: {}, awardId: {}",
				userId, strategyId, ruleModel(), awardId);

		return awardId;
	}

	@Override
	protected String ruleModel() {
		return "default";
	}

}
