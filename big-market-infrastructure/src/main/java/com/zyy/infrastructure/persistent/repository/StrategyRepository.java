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
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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

	@Resource
	private IRuleTreeDao ruleTreeDao;

	@Resource
	private IRuleTreeNodeDao ruleTreeNodeDao;

	@Resource
	private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

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
		return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
	}

	@Override
	public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
//		// 优先从缓存中获取
//		String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
//		RuleTreeVO ruleTreeVO = redisService.getValue(cacheKey);
//		if (null != ruleTreeVO) return ruleTreeVO;
//
//		// 缓存查不到再走库
//		RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
//		List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
//		List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);
//
//		Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
//		for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
//			RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
//					.treeId(ruleTreeNodeLine.getTreeId())
//					.ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
//					.ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
//					.ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
//					.ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
//					.build();
//			//将RuleTreeNodeLineVO对象按照ruleNodeFrom属性分组，存储在ruleTreeNodeLineMap中
//			List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = ruleTreeNodeLineMap.computeIfAbsent(ruleTreeNodeLineVO.getRuleNodeFrom(), k -> new ArrayList<>());
//			ruleTreeNodeLineVOList.add(ruleTreeNodeLineVO);
//		}
//
//		Map<String, RuleTreeNodeVO> treeNodeMap = new HashMap<>();
//		for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
//			RuleTreeNodeVO ruleTreeNodeVo = RuleTreeNodeVO.builder()
//					.treeId(ruleTreeNode.getTreeId())
//					.ruleKey(ruleTreeNode.getRuleKey())
//					.ruleDesc(ruleTreeNode.getRuleDesc())
//					.ruleValue(ruleTreeNode.getRuleValue())
//					.build();
//			treeNodeMap.put(ruleTreeNode.getRuleKey(), ruleTreeNodeVo);
//		}
//
//		ruleTreeVO = RuleTreeVO.builder()
//				.treeId(ruleTree.getTreeId())
//				.treeName(ruleTree.getTreeName())
//				.treeDesc(ruleTree.getTreeDesc())
//				.treeRootRuleNode(ruleTree.getTreeRootRuleKey())
//				.treeNodeMap(treeNodeMap)
//				.build();
//
//		redisService.setValue(cacheKey, ruleTreeVO);
//		return ruleTreeVO;
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
}
