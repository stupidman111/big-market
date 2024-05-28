package com.zyy.domain.strategy.service.raffle;

import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;
import com.zyy.domain.strategy.model.entity.RuleActionEntity;
import com.zyy.domain.strategy.model.entity.RuleMatterEntity;
import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.armory.IStrategyDispatch;
import com.zyy.domain.strategy.service.rule.ILogicFilter;
import com.zyy.domain.strategy.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

	@Resource
	private DefaultLogicFactory logicFactory;

	public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch) {
		super(repository, strategyDispatch);
	}

	@Override
	protected RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics) {

		//获取所有过滤器
		Map<String, ILogicFilter<RuleActionEntity.RaffleBeforeEntity>> logicFilterGroup = logicFactory.openLogicFilter();

		//获取黑名单
		String ruleBlackList = Arrays.stream(logics)
				.filter(str -> str.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
				.findFirst()
				.orElse(null);

		if (StringUtils.isNotBlank(ruleBlackList)) {
			ILogicFilter<RuleActionEntity.RaffleBeforeEntity> blackListLogiccFilter = logicFilterGroup.get(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());//获取黑名单过滤器

			RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
			ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
			ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
			ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
			ruleMatterEntity.setRuleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());

			RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = blackListLogiccFilter.filter(ruleMatterEntity);
			if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) {//是否被黑名单过滤器接管
				return ruleActionEntity;
			}
		}

		//筛选出除了rule_blackList以外的其他规则
		List<String> ruleList = Arrays.stream(logics)
				.filter(s -> !s.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
				.collect(Collectors.toList());

		//过滤其他规则（比如rule_weight）
		RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = null;
		for (String ruleModel : ruleList) {
			ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterGroup.get(ruleModel);//根据rule_model获取对应的规则过滤器

			RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();

			ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
			ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
			ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
			ruleMatterEntity.setRuleModel(ruleModel);

			ruleActionEntity = logicFilter.filter(ruleMatterEntity);

			log.info("抽奖前规则过滤：userId:{}, ruleModel:{}, code:{}, info:{}",
					raffleFactorEntity.getUserId(),
					ruleModel,
					ruleActionEntity.getCode(),
					ruleActionEntity.getInfo());


			if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
		}

		return ruleActionEntity;
	}
}
