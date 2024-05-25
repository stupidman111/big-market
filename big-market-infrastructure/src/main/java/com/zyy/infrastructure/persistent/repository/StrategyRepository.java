package com.zyy.infrastructure.persistent.repository;


import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.infrastructure.persistent.dao.IStrategyAwardDao;
import com.zyy.infrastructure.persistent.po.StrategyAward;
import com.zyy.infrastructure.persistent.redis.IRedisService;
import com.zyy.types.common.Constants;
import org.redisson.api.RMap;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class StrategyRepository implements IStrategyRepository {

	@Resource
	private IStrategyAwardDao strategyAwardDao;

	@Resource
	private IRedisService redisService;

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
	public void storeStrategyAwardSearchRateTable(Long strategyId, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable) {
		// 存储抽奖策略范围值
		redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange);
		// 存储概率查找表
		Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
		cacheRateTable.putAll(strategyAwardSearchRateTable);
	}

	@Override
	public Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey) {
		return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateKey);
	}

	@Override
	public int getRateRange(Long strategyId) {
		return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
	}
}
