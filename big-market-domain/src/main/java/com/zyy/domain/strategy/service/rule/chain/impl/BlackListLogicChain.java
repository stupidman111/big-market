package com.zyy.domain.strategy.service.rule.chain.impl;

import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.zyy.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.zyy.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {

	@Resource
	private IStrategyRepository repository;

	@Override
	public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
		log.info("抽奖责任链-黑名单开始 userId: {}, strategyId: {}, ruleModel: {}", userId, strategyId, ruleModel());

		String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
		String[] splitValue = ruleValue.split(Constants.COLON);
		Integer awardId = Integer.parseInt(splitValue[0]);//黑名单默认奖品

		String[] blackUserIds = splitValue[1].split(Constants.SPLIT);
		for (String blackUserId : blackUserIds) {
			if (userId.equals(blackUserId)) {
				log.info("抽奖责任链-黑名单接管 userId: {}, startegyId: {}, ruleModel: {}, awardId: {}",
						userId, strategyId, ruleModel(), awardId);
				return DefaultChainFactory.StrategyAwardVO.builder()
						.awardId(awardId)
						.logicModel(ruleModel())
						.build();
			}
		}

		//过滤其他规则
		log.info("抽奖责任链-黑名单放行 userId: {}, strategyId: {}, ruleModel: {}",
				userId, strategyId, ruleModel());

		return next().logic(userId, strategyId);
	}

	@Override
	protected String ruleModel() {
		return DefaultChainFactory.LogicModel.RULE_BLACKLIST.getCode();
	}
}
