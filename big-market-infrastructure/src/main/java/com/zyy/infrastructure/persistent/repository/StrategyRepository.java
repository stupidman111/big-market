package com.zyy.infrastructure.persistent.repository;


import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.domain.strategy.model.entity.StrategyEntity;
import com.zyy.domain.strategy.model.entity.StrategyRuleEntity;
import com.zyy.domain.strategy.model.valobj.*;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.infrastructure.persistent.dao.*;
import com.zyy.infrastructure.persistent.po.*;
import com.zyy.infrastructure.persistent.redis.IRedisService;
import com.zyy.types.common.Constants;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zyy.types.enums.ResponseCode.UN_ASSEMBLED_STRATEGY_ARMORY;


@Slf4j
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

	@Resource
	private IRuleTreeDao ruleTreeDao;

	@Resource
	private IRuleTreeNodeDao ruleTreeNodeDao;

	@Resource
	private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

	@Resource
	private IRaffleActivityDao raffleActivityDao;

	@Resource
	private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;
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
					.awardTitle(strategyAward.getAwardTitle())
					.awardSubtitle(strategyAward.getAwardSubTitle())
					.sort(strategyAward.getSort())
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
	public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
		StrategyRule strategyRule = new StrategyRule();
		strategyRule.setStrategyId(strategyId);
		strategyRule.setAwardId(awardId);
		strategyRule.setRuleModel(ruleModel);
		return strategyRuleDao.queryStrategyRuleValue(strategyRule);
	}

	@Override
	public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
		return queryStrategyRuleValue(strategyId, null, ruleModel);
	}

	@Override
	public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId) {
		StrategyAward strategyAward = new StrategyAward();
		strategyAward.setStrategyId(strategyId);
		strategyAward.setAwardId(awardId);
		String ruleModels = strategyAwardDao.queryStrategyAwardRuleModels(strategyAward);
		return StrategyAwardRuleModelVO.builder().ruleModels(ruleModels).build();
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
		String cacheKey = Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key;
		if (!redisService.isExists(cacheKey)) {
			throw new AppException(UN_ASSEMBLED_STRATEGY_ARMORY.getCode(), cacheKey + Constants.COLON + UN_ASSEMBLED_STRATEGY_ARMORY.getInfo());
		}
		return redisService.getValue(cacheKey);
	}

	@Override
	public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
		// 优先从缓存获取
		String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
		RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
		if (null != ruleTreeVOCache) return ruleTreeVOCache;

		// 从数据库获取
		RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
		List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
		List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);

		// 1. tree node line 转换Map结构
		Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
		for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
			RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
					.treeId(ruleTreeNodeLine.getTreeId())
					.ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
					.ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
					.ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
					.ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
					.build();

			List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = ruleTreeNodeLineMap.computeIfAbsent(ruleTreeNodeLine.getRuleNodeFrom(), k -> new ArrayList<>());
			ruleTreeNodeLineVOList.add(ruleTreeNodeLineVO);
		}

		// 2. tree node 转换为Map结构
		Map<String, RuleTreeNodeVO> treeNodeMap = new HashMap<>();
		for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
			RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
					.treeId(ruleTreeNode.getTreeId())
					.ruleKey(ruleTreeNode.getRuleKey())
					.ruleDesc(ruleTreeNode.getRuleDesc())
					.ruleValue(ruleTreeNode.getRuleValue())
					.treeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNode.getRuleKey()))
					.build();
			treeNodeMap.put(ruleTreeNode.getRuleKey(), ruleTreeNodeVO);
		}

		// 3. 构建 Rule Tree
		RuleTreeVO ruleTreeVODB = RuleTreeVO.builder()
				.treeId(ruleTree.getTreeId())
				.treeName(ruleTree.getTreeName())
				.treeDesc(ruleTree.getTreeDesc())
				.treeRootRuleNode(ruleTree.getTreeRootRuleKey())
				.treeNodeMap(treeNodeMap)
				.build();

		redisService.setValue(cacheKey, ruleTreeVODB);
		return ruleTreeVODB;
	}

	//缓存奖品库存
	@Override
	public void cacheStrategyAwardCount(String cacheKey, Integer awardCount) {
		if (redisService.isExists(cacheKey)) return;
		redisService.setAtomicLong(cacheKey, awardCount);
	}

	//缓存key，decr方式扣减库存
	@Override
	public Boolean subtractionAwardStock(String cacheKey) {
		long surplus = redisService.decr(cacheKey);

		if (surplus == 0) {//库存等于 0 个，发送 MQ消息直接清空

		}

		if (surplus < 0) {//库存小于0，恢复为0个
			redisService.setValue(cacheKey, 0);
			return false;
		}

		//库存正常：对所扣减的库存数加锁（表示当前这个数量的库存已经扣减），这样后续有恢复库存等操作也不会导致超卖（因为消耗过的库存数，都加了锁）
		String lockKey = cacheKey + Constants.UNDERLINE + (surplus + 1);
		Boolean lock = redisService.setNx(lockKey);
		if (!lock) {
			log.info("策略奖品库存加锁失败：{}", lockKey);
		}
		return lock;
	}

	//写入奖品库存消费队列
	@Override
	public void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO) {
		String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY;
		RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
		RDelayedQueue<StrategyAwardStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
		delayedQueue.offer(strategyAwardStockKeyVO, 3, TimeUnit.SECONDS);
	}

	//获取奖品库存消费队列
	@Override
	public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
		String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY;
		RBlockingQueue<StrategyAwardStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
		return destinationQueue.poll();
	}

	//更新奖品库存消耗
	@Override
	public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
		StrategyAward strategyAward = new StrategyAward();
		strategyAward.setStrategyId(strategyId);
		strategyAward.setAwardId(awardId);
		strategyAwardDao.updateStrategyAwardStock(strategyAward);
	}

	@Override
	public StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId) {

		//优先从缓存中获取
		String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId + Constants.UNDERLINE + awardId;
		StrategyAwardEntity strategyAwardEntity = redisService.getValue(cacheKey);
		if (null != strategyAwardEntity) return strategyAwardEntity;

		//缓存不存在则走库
		StrategyAward strategyAward = new StrategyAward();
		strategyAward.setStrategyId(strategyId);
		strategyAward.setAwardId(awardId);
		StrategyAward strategyAwardRes = strategyAwardDao.queryStrategyAward(strategyAward);

		strategyAwardEntity = StrategyAwardEntity.builder()
				.strategyId(strategyAwardRes.getStrategyId())
				.awardId(strategyAwardRes.getAwardId())
				.awardTitle(strategyAwardRes.getAwardTitle())
				.awardSubtitle(strategyAwardRes.getAwardSubTitle())
				.awardCount(strategyAwardRes.getAwardCount())
				.awardCountSurplus(strategyAwardRes.getAwardCountSurplus())
				.awardRate(strategyAwardRes.getAwardRate())
				.sort(strategyAwardRes.getSort())
				.build();

		redisService.setValue(cacheKey, strategyAwardEntity);

		return strategyAwardEntity;
	}

	@Override
	public Long queryStrategyIdByActivityId(Long activityId) {
		return raffleActivityDao.queryStrategyIdByActivityId(activityId);
	}

	@Override
	public Integer queryTodayUserRaffleCount(String userId, Long strategyId) {
		//活动 ID
		Long activityId = raffleActivityDao.queryActivityIdByStrategyId(strategyId);

		RaffleActivityAccountDay raffleActivityAccountDayReq = new RaffleActivityAccountDay();
		raffleActivityAccountDayReq.setUserId(userId);
		raffleActivityAccountDayReq.setActivityId(activityId);
		raffleActivityAccountDayReq.setDay(raffleActivityAccountDayReq.currentDay());

		//统计今日剩余次数
		RaffleActivityAccountDay raffleActivityAccountDay = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(raffleActivityAccountDayReq);
		if (null == raffleActivityAccountDay) return 0;
		return raffleActivityAccountDay.getDayCount() - raffleActivityAccountDay.getDayCountSurplus();
	}


}
