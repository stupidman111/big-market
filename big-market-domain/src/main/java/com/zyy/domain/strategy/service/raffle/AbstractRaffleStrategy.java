package com.zyy.domain.strategy.service.raffle;

import com.zyy.domain.strategy.model.entity.RaffleAwardEntity;
import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;
import com.zyy.domain.strategy.model.entity.RuleActionEntity;
import com.zyy.domain.strategy.model.entity.StrategyEntity;
import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.IRaffleStrategy;
import com.zyy.domain.strategy.service.armory.IStrategyDispatch;
import com.zyy.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

	protected IStrategyRepository repository;

	protected IStrategyDispatch strategyDispatch;

	public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch) {
		this.repository = repository;
		this.strategyDispatch = strategyDispatch;
	}

	@Override
	public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {

		//获取参数
		String userId = raffleFactorEntity.getUserId();
		Long strategyId = raffleFactorEntity.getStrategyId();

		if (null == strategyId || StringUtils.isBlank(userId)) {
			throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
		}

		//策略查询
		StrategyEntity strategy = repository.queryStrategyEntityByStrategyById(strategyId);

		//抽奖前，规则过滤
		RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity =
				this.doCheckRaffleBeforeLogic(//模板模式处理
						RaffleFactorEntity.
								builder().
								userId(userId)
								.strategyId(strategyId)
								.build(),
						strategy.ruleModels());

		//查看是否被某个过滤规则接管，并返回对应的抽奖奖品结果实体
		if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())) {
			if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())) {//被黑名单规则接管
				return RaffleAwardEntity.builder()
						.awardId(ruleActionEntity.getData().getAwardId())
						.build();
			} else if (DefaultLogicFactory.LogicModel.RULE_WEIGHT.getCode().equals(ruleActionEntity.getRuleModel())) {//被权重规则接管
				RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
				String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
				Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
				return RaffleAwardEntity.builder()
						.awardId(awardId)
						.build();
			}
		}

		//未被接管（过滤通过），走默认抽奖流程
		Integer awardId = strategyDispatch.getRandomAwardId(strategyId);

		return RaffleAwardEntity.builder()
				.awardId(awardId)
				.build();
	}

	protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(
			RaffleFactorEntity raffleFactorEntity, String... logics);
}
