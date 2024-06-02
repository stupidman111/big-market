package com.zyy.domain.strategy.service;

import com.zyy.domain.strategy.model.entity.RaffleAwardEntity;
import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;
import com.zyy.domain.strategy.model.entity.RuleActionEntity;
import com.zyy.domain.strategy.model.entity.StrategyEntity;
import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.zyy.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.IRaffleStrategy;
import com.zyy.domain.strategy.service.armory.IStrategyDispatch;
import com.zyy.domain.strategy.service.rule.chain.ILogicChain;
import com.zyy.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.zyy.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

	protected IStrategyRepository repository;

	protected IStrategyDispatch strategyDispatch;

	protected DefaultChainFactory defaultChainFactory;

	public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory) {
		this.repository = repository;
		this.strategyDispatch = strategyDispatch;
		this.defaultChainFactory = defaultChainFactory;
	}

	@Override
	public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
		// 参数校验
		String userId = raffleFactorEntity.getUserId();
		Long strategyId = raffleFactorEntity.getStrategyId();
		if (null == strategyId || StringUtils.isBlank(userId)) {
			throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
		}

		//获取责任链进行抽奖
		ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
		Integer awardId = logicChain.logic(userId, strategyId);

		//拿到奖品ID，查询奖品的rule_model，进行诸如次数锁的过滤
		StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);

		RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(
				RaffleFactorEntity.builder()
						.userId(userId)
						.strategyId(strategyId)
						.awardId(awardId)
						.build(),
				strategyAwardRuleModelVO.raffleCenterRuleModelList());

		//是否被接管
		if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())) {
			log.info("【临时日志（被接管） 走 rule_luck_award兜底");
			return RaffleAwardEntity.builder()
					.awardDesc("被接管） 走 rule_luck_award兜底")
					.build();
		}

		return RaffleAwardEntity.builder()
				.awardId(awardId)
				.build();

	}

	protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(
			RaffleFactorEntity raffleFactorEntity, String... logics);
}
