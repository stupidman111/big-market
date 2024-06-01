package com.zyy.domain.strategy.service.rule.impl;

import com.zyy.domain.strategy.model.entity.RuleActionEntity;
import com.zyy.domain.strategy.model.entity.RuleMatterEntity;
import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.annotation.LogicStrategy;
import com.zyy.domain.strategy.service.rule.ILogicFilter;
import com.zyy.domain.strategy.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Rule;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

	@Resource
	private IStrategyRepository repository;

	private Long userRaffleCount = 0L;


	@Override
	public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
		log.info("规则过滤-次数锁 userId:{} strategyId:{} ruleModel:{}",
				ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

		String ruleValue = repository.queryStrategyRuleValue(
				ruleMatterEntity.getStrategyId(),
				ruleMatterEntity.getAwardId(),
				ruleMatterEntity.getRuleModel());

		long raffleCount = Long.parseLong(ruleValue);

		if (userRaffleCount >= raffleCount) {//满足次数锁，放行
			return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
					.code(RuleLogicCheckTypeVO.ALLOW.getCode())
					.info(RuleLogicCheckTypeVO.ALLOW.getInfo())
					.build();
		}

		return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
				.code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
				.info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
				.build();
	}
}
