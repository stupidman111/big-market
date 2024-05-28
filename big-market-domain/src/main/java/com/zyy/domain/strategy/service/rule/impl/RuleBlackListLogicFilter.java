package com.zyy.domain.strategy.service.rule.impl;


import com.zyy.domain.strategy.model.entity.RuleActionEntity;
import com.zyy.domain.strategy.model.entity.RuleMatterEntity;
import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.annotation.LogicStrategy;
import com.zyy.domain.strategy.service.rule.ILogicFilter;
import com.zyy.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.zyy.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBlackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

	@Resource
	private IStrategyRepository repository;

	@Override
	public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
		log.info("规则过滤-黑名单 userId:{} strategyId:{} ruleModel:{}",
				ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

		String userId = ruleMatterEntity.getUserId();

		String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),
				ruleMatterEntity.getAwardId(),
				ruleMatterEntity.getRuleModel());

		//分解出黑名单用户，以及黑名单的奖品
		//黑名单抽奖：100:user1,user2,user3
		String[] splitRuleValue = ruleValue.split(Constants.COLON);
		Integer awardId = Integer.parseInt(splitRuleValue[0]);

		//看当前抽奖用户是否是黑名单用户
		String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
		for (String userBlackId : userBlackIds) {
			if (userId.equals(userBlackId)) {//当前用户是黑名单用户，接管，并返回动作实体
				return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
						.ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
						.data(RuleActionEntity.RaffleBeforeEntity.builder()
								.strategyId(ruleMatterEntity.getStrategyId())
								.awardId(awardId)
								.build())
						.code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
						.info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
						.build();

			}
		}

		//当前用户不是黑名单用户，放行
		return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
				.code(RuleLogicCheckTypeVO.ALLOW.getCode())
				.info(RuleLogicCheckTypeVO.ALLOW.getInfo())
				.build();
	}
}
