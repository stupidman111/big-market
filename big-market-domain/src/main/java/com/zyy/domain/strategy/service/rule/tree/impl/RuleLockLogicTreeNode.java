package com.zyy.domain.strategy.service.rule.tree.impl;

import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.zyy.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 次数锁节点
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

	//一开始这里是写死然后通过 mock修改的，有了活动领域后就不需要了
	//private Long userRaffleCount = 10L;

	@Resource
	private IStrategyRepository repository;

	@Override
	public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue, Date endDateTime) {
		log.info("规则过滤-次数锁 userId:{}, strategyId:{}, awardId:{}", userId, strategyId, awardId);

		long raffleCount = 0L;
		try {
			raffleCount = Long.valueOf(ruleValue);
		} catch (Exception e) {
			throw new RuntimeException("规则过滤-次数锁异常 ruleValue: " + ruleValue + "配置不正确");
		}

		//查询用户抽奖次数
		Integer userRaffleCount = repository.queryTodayUserRaffleCount(userId, strategyId);

		//用户抽奖次数大于规定值，放行
		if (userRaffleCount > raffleCount) {
			return DefaultTreeFactory.TreeActionEntity.builder()
					.ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
					.build();
		}

		//否则拦截
		return DefaultTreeFactory.TreeActionEntity.builder()
				.ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
				.build();
	}
}
