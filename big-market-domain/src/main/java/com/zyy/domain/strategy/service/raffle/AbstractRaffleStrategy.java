package com.zyy.domain.strategy.service.raffle;

import com.zyy.domain.strategy.model.entity.RaffleAwardEntity;
import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;
import com.zyy.domain.strategy.model.entity.RuleActionEntity;
import com.zyy.domain.strategy.model.entity.StrategyEntity;
import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.zyy.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
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
		// 1. 参数校验
		String userId = raffleFactorEntity.getUserId();
		Long strategyId = raffleFactorEntity.getStrategyId();
		if (null == strategyId || StringUtils.isBlank(userId)) {
			throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
		}

		// 2. 策略查询
		StrategyEntity strategy = repository.queryStrategyEntityByStrategyById(strategyId);

		// 3. 抽奖前 - 规则过滤
		RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionBeforeEntity = this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder()
				.userId(userId)
				.strategyId(strategyId)
				.build(), strategy.ruleModels());

		if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionBeforeEntity.getCode())) {
			if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionBeforeEntity.getRuleModel())) {
				// 黑名单返回固定的奖品ID
				return RaffleAwardEntity.builder()
						.awardId(ruleActionBeforeEntity.getData().getAwardId())
						.build();
			} else if (DefaultLogicFactory.LogicModel.RULE_WEIGHT.getCode().equals(ruleActionBeforeEntity.getRuleModel())) {
				// 权重根据返回的信息进行抽奖
				RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionBeforeEntity.getData();
				String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
				Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
				return RaffleAwardEntity.builder()
						.awardId(awardId)
						.build();
			}
		}

		// 4. 默认抽奖流程
		Integer awardId = strategyDispatch.getRandomAwardId(strategyId);

		// 5. 查询奖品规则「抽奖中（拿到奖品ID时，过滤规则）、抽奖后（扣减完奖品库存后过滤，抽奖中拦截和无库存则走兜底）」
		StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);

		// 6. 抽奖中 - 规则过滤
		RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
				.userId(userId)
				.strategyId(strategyId)
				.awardId(awardId)
				.build(), strategyAwardRuleModelVO.raffleCenterRuleModelList());

		if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())){
			log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
			return RaffleAwardEntity.builder()
					.awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
					.build();
		}

		return RaffleAwardEntity.builder()
				.awardId(awardId)
				.build();
	}

	protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(
			RaffleFactorEntity raffleFactorEntity, String... logics);

	protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(
			RaffleFactorEntity raffleFactorEntity, String... logics);
}
