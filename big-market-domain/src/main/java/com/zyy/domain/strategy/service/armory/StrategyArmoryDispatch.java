package com.zyy.domain.strategy.service.armory;

import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.domain.strategy.model.entity.StrategyEntity;
import com.zyy.domain.strategy.model.entity.StrategyRuleEntity;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Service
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {

	@Resource
	private IStrategyRepository repository;

	@Override
	public boolean assembleLotteryStrategy(Long strategyId) {
		//1.根据策略ID，获取【策略-奖品】实体-列表
		List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
		assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

		//2.权重策略配置
		StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyById(strategyId);
		String ruleWeight = strategyEntity.getRuleWeight();
		if (null == ruleWeight) return true;

		StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
		if (null == strategyRuleEntity) {
			throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
		}
		Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
		Set<String> keys = ruleWeightValueMap.keySet();
		//去除掉不在rule_weight规则中的awardId
		for (String key : keys) {
			List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
			ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
			strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
			assembleLotteryStrategy(String.valueOf(String.valueOf(strategyId).concat("_").concat(key)), strategyAwardEntitiesClone);
		}

		return true;
	}

	private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
		//2.在【策略-奖品】实体-列表中获取最小的概率
		BigDecimal minAwardRate = strategyAwardEntities.stream()
				.map(StrategyAwardEntity::getAwardRate)
				.min(BigDecimal::compareTo)
				.orElse(BigDecimal.ZERO);

		//3.计算【策略-奖品】实体-列表 的总概率
		BigDecimal totalAwardRate = strategyAwardEntities.stream()
				.map(StrategyAwardEntity::getAwardRate)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		//4. 总概率 / 最小概率，将概率以最小概率为分段范围，比如：三个实体对应概率：0.2，0.3，0.5，总概率为1，1 / 0.2 = 5，那么基准就有5个分段
		BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

		//5. 将所有实体的概率，按照 概率 * 分段范围 获取实际有n个实体的id存到list中；
		List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
		for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
			Integer awardId = strategyAward.getAwardId();
			BigDecimal awardRate = strategyAward.getAwardRate();
			for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
				strategyAwardSearchRateTables.add(awardId);
			}
		}

		//6. 乱序操作
		Collections.shuffle(strategyAwardSearchRateTables);

		//7. 生成map集合，
		Map<Integer, Integer> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
		for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
			shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
		}

		repository.storeStrategyAwardSearchRateTable(key, shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);
	}

	@Override
	public Integer getRandomAwardId(Long strategyId) {
		int rateRange = repository.getRateRange(strategyId);
		return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
	}

	@Override
	public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
		String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
		int rateRange = repository.getRateRange(key);
		return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
	}
}
