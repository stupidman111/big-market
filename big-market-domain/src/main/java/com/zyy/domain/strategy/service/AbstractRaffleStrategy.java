package com.zyy.domain.strategy.service;

import com.zyy.domain.strategy.model.entity.RaffleAwardEntity;
import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.armory.IStrategyDispatch;
import com.zyy.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.zyy.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

	protected IStrategyRepository repository;

	protected IStrategyDispatch strategyDispatch;

	protected DefaultChainFactory defaultChainFactory;

	protected DefaultTreeFactory defaultTreeFactory;

	public AbstractRaffleStrategy(IStrategyRepository repository,
								  IStrategyDispatch strategyDispatch,
								  DefaultChainFactory defaultChainFactory,
								  DefaultTreeFactory defaultTreeFactory) {
		this.repository = repository;
		this.strategyDispatch = strategyDispatch;
		this.defaultChainFactory = defaultChainFactory;
		this.defaultTreeFactory = defaultTreeFactory;
	}

	@Override
	public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
		// 1. 参数校验
		String userId = raffleFactorEntity.getUserId();
		Long strategyId = raffleFactorEntity.getStrategyId();
		if (null == strategyId || StringUtils.isBlank(userId)) {
			throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
		}

		// 2. 获取责任链进行抽奖（拿到的是初步抽奖奖品的信息，黑名单、权重抽奖直接返回结果，默认抽奖的结果需要进行规则树判断）
		DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = raffleLogicChain(userId, strategyId);
		log.info("抽奖策略计算-责任链 {} {} {} {}", userId, strategyId, chainStrategyAwardVO.getAwardId(), chainStrategyAwardVO.getLogicModel());
		if (!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(chainStrategyAwardVO.getLogicModel())) {
			return RaffleAwardEntity.builder()
					.awardId(chainStrategyAwardVO.getAwardId())
					.build();
		}

		// 3. 获取规则树进行抽奖（根据默认抽奖得到的奖品信息，进行次数锁、库存、兜底判断）
		DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = raffleLogicTree(userId, strategyId, chainStrategyAwardVO.getAwardId());
		log.info("抽奖策略计算-规则树 {} {} {} {}", userId, strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());

		// 4. 返回抽奖结果
		return RaffleAwardEntity.builder()
				.awardId(treeStrategyAwardVO.getAwardId())
				.awardConfig(treeStrategyAwardVO.getAwardRuleValue())
				.build();

	}

	public abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId);

	public abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId);
}
