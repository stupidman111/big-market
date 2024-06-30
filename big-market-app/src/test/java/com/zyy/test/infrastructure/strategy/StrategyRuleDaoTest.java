package com.zyy.test.infrastructure.strategy;

import com.alibaba.fastjson.JSON;
import com.zyy.infrastructure.persistent.dao.IStrategyRuleDao;
import com.zyy.infrastructure.persistent.po.StrategyRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyRuleDaoTest {

	@Resource
	private IStrategyRuleDao strategyRuleDao;

	@Test
	public void test_queryStrategyRuleList() {
		List<StrategyRule> strategyRules = strategyRuleDao.queryStrategyRuleList();
		log.info("测试结果：{}", JSON.toJSONString(strategyRules));
	}

	@Test
	public void test_queryStrategyRule() {
		StrategyRule strategyRule = new StrategyRule();
		strategyRule.setStrategyId(100001L);
		strategyRule.setRuleModel("rule_weight");
		strategyRule = strategyRuleDao.queryStrategyRule(strategyRule);
		log.info("测试结果：{}", JSON.toJSONString(strategyRule));
	}

	@Test
	public void test_queryStrategyRuleValue() {
		StrategyRule strategyRule = new StrategyRule();
		strategyRule.setStrategyId(100001L);
		strategyRule.setAwardId(101);
		strategyRule.setRuleModel("rule_random");
		String ruleValue = strategyRuleDao.queryStrategyRuleValue(strategyRule);
		System.out.println(ruleValue);
	}
}
