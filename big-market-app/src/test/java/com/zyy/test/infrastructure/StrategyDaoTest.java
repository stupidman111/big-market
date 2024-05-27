package com.zyy.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.zyy.infrastructure.persistent.dao.IAwardDao;
import com.zyy.infrastructure.persistent.dao.IStrategyDao;
import com.zyy.infrastructure.persistent.po.Award;
import com.zyy.infrastructure.persistent.po.Strategy;
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
public class StrategyDaoTest {
	@Resource
	private IStrategyDao strategyDao;

	@Test
	public void test_queryStrategyList() {
		List<Strategy> strategyList = strategyDao.queryStrategyList();
		log.info("测试结果：{}", JSON.toJSONString(strategyList));
	}

	@Test
	public void test_queryStrategyByStrategyId() {
		Strategy strategy = strategyDao.queryStrategyByStrategyId(100001L);
		log.info("测试结果：{}", JSON.toJSONString(strategy.getStrategyId()));
		log.info("测试结果：{}", JSON.toJSONString(strategy.getRuleModels()));
		log.info("测试结果：{}", JSON.toJSONString(strategy.getStrategyDesc()));
		log.info("测试结果：{}", JSON.toJSONString(strategy));
	}

}
