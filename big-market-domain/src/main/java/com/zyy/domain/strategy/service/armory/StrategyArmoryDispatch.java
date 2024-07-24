package com.zyy.domain.strategy.service.armory;

import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.domain.strategy.model.entity.StrategyEntity;
import com.zyy.domain.strategy.model.entity.StrategyRuleEntity;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.types.common.Constants;
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

	private final SecureRandom secureRandom = new SecureRandom();

	@Override
	public boolean assembleLotteryStrategy(Long strategyId) {
		//1.根据策略ID，获取【策略-奖品】实体-列表
		List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

		//2.缓存奖品库存
		for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
			Integer awardId = strategyAwardEntity.getAwardId();
			Integer awardCount = strategyAwardEntity.getAwardCount();
			cacheStrategyAwardCount(strategyId, awardId, awardCount);
		}

		//3.1 默认装配配置【全量】
		assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);


		//3.2 权重策略配置【权重】
		StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyById(strategyId);
		String ruleWeight = strategyEntity.getRuleWeight();
		if (null == ruleWeight) return true;

		StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
		if (null == strategyRuleEntity) {
			throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
		}
		Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
		for (String key : ruleWeightValueMap.keySet()) {
			List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
			ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
			strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
			assembleLotteryStrategy(String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(key), strategyAwardEntitiesClone);
		}

		return true;
	}

	@Override
	public boolean assembleLotteryStrategyByActivityId(Long activityId) {
		Long strategyId = repository.queryStrategyIdByActivityId(activityId);
		return assembleLotteryStrategy(strategyId);
	}

	private void cacheStrategyAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
		String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
		repository.cacheStrategyAwardCount(cacheKey, awardCount);
	}

	private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
		//1. 在【策略-奖品】实体-列表中获取最小的概率
		BigDecimal minAwardRate = strategyAwardEntities.stream()
				.map(StrategyAwardEntity::getAwardRate)
				.min(BigDecimal::compareTo)
				.orElse(BigDecimal.ZERO);

		//2. 循环计算找到概率范围值
		BigDecimal rateRange = BigDecimal.valueOf(convert(minAwardRate.doubleValue()));


		//5. 将所有实体的概率，按照 概率 * 分段范围 获取实际有n个实体的id存到list中；
		List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
		for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
			Integer awardId = strategyAward.getAwardId();
			BigDecimal awardRate = strategyAward.getAwardRate();
			for (int i = 0; i < rateRange.multiply(awardRate).intValue(); i++) {
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

	private double convert(double min) {
		double current = min;
		double max = 1;
		while (current < 1) {
			current = current * 10;
			max = max * 10;
		}
		return max;
	}

	@Override
	public Integer getRandomAwardId(Long strategyId) {
		int rateRange = repository.getRateRange(strategyId);
		return repository.getStrategyAwardAssemble(String.valueOf(strategyId), secureRandom.nextInt(rateRange));
	}

	@Override
	public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
		String key = String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(ruleWeightValue);
		return getRandomAwardId(key);
	}

	@Override
	public Integer getRandomAwardId(String key) {
		int rateRange = repository.getRateRange(key);
		return repository.getStrategyAwardAssemble(key, secureRandom.nextInt(rateRange));
	}

	@Override
	public Boolean subtractionAwardStock(Long strategyId, Integer awardId, Date endDateTime) {
		String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
		return repository.subtractionAwardStock(cacheKey, endDateTime);
	}
}
