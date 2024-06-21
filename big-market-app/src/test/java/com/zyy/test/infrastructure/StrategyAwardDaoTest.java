package com.zyy.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.infrastructure.persistent.dao.IStrategyAwardDao;
import com.zyy.infrastructure.persistent.po.StrategyAward;
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
public class StrategyAwardDaoTest {

	@Resource
	private IStrategyAwardDao strategyAwardDao;

	@Test
	public void test_queryStrategyAwardList() {
		List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardList();
		log.info("测试结果：{}", JSON.toJSONString(strategyAwards));
	}

	@Test
	public void test_queryStrategyAwardListByStrategyId() {
		List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(100001L);
		log.info("测试结果：{}", JSON.toJSONString(strategyAwards));
	}

	@Test
	public void test_queryStrategyAwardRuleModels() {
		StrategyAward strategyAward = new StrategyAward();
		strategyAward.setStrategyId(100001L);
		strategyAward.setAwardId(101);
		String ruleModels = strategyAwardDao.queryStrategyAwardRuleModels(strategyAward);
		log.info("测试结果：{}", JSON.toJSONString(ruleModels));
	}

	@Test
	public void test_updateStrategyAwardStock() {
		StrategyAward strategyAward = new StrategyAward();
		strategyAward.setStrategyId(100001L);
		strategyAward.setAwardId(101);
		strategyAwardDao.updateStrategyAwardStock(strategyAward);

	}

	@Test
	public void test_queryStrategyAward() {
		StrategyAward strategyAward = new StrategyAward();
		strategyAward.setStrategyId(100001L);
		strategyAward.setAwardId(101);

		StrategyAward strategyAwardRes = strategyAwardDao.queryStrategyAward(strategyAward);
		log.info("{}", JSON.toJSONString(strategyAwardRes));
	}
}
