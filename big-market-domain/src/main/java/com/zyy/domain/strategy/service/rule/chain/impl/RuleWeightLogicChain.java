package com.zyy.domain.strategy.service.rule.chain.impl;

import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.armory.IStrategyDispatch;
import com.zyy.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.zyy.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

	@Resource
	private IStrategyRepository repository;

	@Resource
	protected IStrategyDispatch strategyDispatch;

	public Long userScore = 0L;


	@Override
	public Integer logic(String userId, Long strategyId) {
		log.info("抽奖责任链-权重开始 userId: {}, strategyId: {}, ruleModel: {}",
				userId, strategyId, ruleModel());

		//获取根据strategyId获取权重规则列表
		String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());

		//解析当前rule_weight的rule_value
		Map<Long, String> ruleValueGroup = analyticalRuleValue(ruleValue);
		if (null == ruleValueGroup || ruleValueGroup.isEmpty()) return null;

		List<Long> ruleValueSortedKeys = new ArrayList<>(ruleValueGroup.keySet());
		Collections.sort(ruleValueSortedKeys);

		Long nextValue = ruleValueSortedKeys.stream()
				.sorted(Comparator.reverseOrder())
				.filter(key -> userScore >= key)
				.findFirst()
				.orElse(null);

		if (null != nextValue) {
			Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleValueGroup.get(nextValue));
			log.info("抽奖责任链-权重接管 userId: {}, strategyId: {}, ruleModel: {}, awardId: {}",
					userId, strategyId, ruleModel(), awardId);
			return awardId;
		}

		log.info("抽奖责任链-权重放行 userId: {}, startegyId:{}, ruleModel: {}",
				userId, strategyId, ruleModel());
		return next().logic(userId, strategyId);
	}

	@Override
	protected String ruleModel() {
		return "rule_weight";
	}

	private Map<Long, String> analyticalRuleValue(String ruleValue) {
		//解析诸如4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
		String[] weightWithAwardsList = ruleValue.split(Constants.SPACE);
		Map<Long, String> ans = new HashMap<>();
		for (String weightWithAwards : weightWithAwardsList) {
			if (null == weightWithAwards || 0 == weightWithAwards.length()) return ans;

			String[] weightWithAwardsSplit = weightWithAwards.split(Constants.COLON);

			if (2 != weightWithAwardsSplit.length)
				throw new IllegalArgumentException("rule_weight invalid input format" + ruleValue);

			ans.put(Long.parseLong(weightWithAwardsSplit[0]), weightWithAwards);
		}
		return ans;
	}
}
