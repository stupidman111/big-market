package com.zyy.infrastructure.persistent.repository;


import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.domain.strategy.model.entity.StrategyEntity;
import com.zyy.domain.strategy.model.entity.StrategyRuleEntity;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.infrastructure.persistent.dao.IStrategyAwardDao;
import com.zyy.infrastructure.persistent.dao.IStrategyDao;
import com.zyy.infrastructure.persistent.dao.IStrategyRuleDao;
import com.zyy.infrastructure.persistent.po.Strategy;
import com.zyy.infrastructure.persistent.po.StrategyAward;
import com.zyy.infrastructure.persistent.po.StrategyRule;
import com.zyy.infrastructure.persistent.redis.IRedisService;
import com.zyy.types.common.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class StrategyRepository implements IStrategyRepository {

	@Resource
	private IRedisService redisService;

	@Resource
	private IStrategyAwardDao strategyAwardDao;

	@Resource
	private IStrategyDao strategyDao;

	@Resource
	private IStrategyRuleDao strategyRuleDao;

	@Override
	public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {

		// 优先从缓存中获取
		String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
		List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
		if (null != strategyAwardEntities && !strategyAwardEntities.isEmpty()) return strategyAwardEntities;

		//缓存不存在，则从库中获取
		List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
		strategyAwardEntities = new ArrayList<>(strategyAwards.size());
		for (StrategyAward strategyAward : strategyAwards) {
			StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
					.strategyId(strategyAward.getStrategyId())
					.awardId(strategyAward.getAwardId())
					.awardCount(strategyAward.getAwardCount())
					.awardCountSurplus(strategyAward.getAwardCountSurplus())
					.awardRate(strategyAward.getAwardRate())
					.build();
			strategyAwardEntities.add(strategyAwardEntity);
		}
		redisService.setValue(cacheKey, strategyAwardEntities);
		return strategyAwardEntities;
	}

	@Override
	public void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable) {
		// 存储抽奖策略范围值
		redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange);
		// 存储概率查找表
		Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
		cacheRateTable.putAll(strategyAwardSearchRateTable);
	}

	@Override
	public StrategyEntity queryStrategyEntityByStrategyById(Long strategyId) {
		//先查库，在查表
		String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
		StrategyEntity strategyEntity = redisService.getValue(cacheKey);
		if (null != strategyEntity) return strategyEntity;
		Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);
		strategyEntity = StrategyEntity.builder()
				.strategyId(strategy.getStrategyId())
				.strategyDesc(strategy.getStrategyDesc())
				.ruleModels(strategy.getRuleModels())
				.build();
		redisService.setValue(cacheKey, strategyEntity);
		return strategyEntity;
	}

	@Override
	public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight) {
		//
		StrategyRule strategyRuleReq = new StrategyRule();
		strategyRuleReq.setStrategyId(strategyId);
		strategyRuleReq.setRuleModel(ruleWeight);
		StrategyRule strategyRule = strategyRuleDao.queryStrategyRule(strategyRuleReq);
		return StrategyRuleEntity.builder()
				.strategyId(strategyRule.getStrategyId())
				.awardId(strategyRule.getAwardId())
				.ruleType(strategyRule.getRuleType())
				.ruleModel(strategyRule.getRuleModel())
				.ruleValue(strategyRule.getRuleValue())
				.ruleDesc(strategyRule.getRuleDesc())
				.build();
	}

	@Override
	public Integer getStrategyAwardAssemble(String key, Integer rateKey) {
		return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, rateKey);
	}

	@Override
	public int getRateRange(Long strategyId) {
		return getRateRange(String.valueOf(strategyId));
	}

	@Override
	public int getRateRange(String key) {
		return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
	}

}
