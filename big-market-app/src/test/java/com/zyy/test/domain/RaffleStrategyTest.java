package com.zyy.test.domain;


import com.alibaba.fastjson.JSON;
import com.zyy.domain.strategy.model.entity.RaffleAwardEntity;
import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;
import com.zyy.domain.strategy.service.IRaffleStrategy;
import com.zyy.domain.strategy.service.rule.impl.RuleWeightLogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyTest {

	@Resource
	private IRaffleStrategy raffleStrategy;

	@Resource
	private RuleWeightLogicFilter ruleWeightLogicFilter;

	@Before
	public void setUp() {
		ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 40500L);
	}

	@Test
	public void test_performRaffle() {
		RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
				.userId("zy")
				.strategyId(100001L)
				.build();

		RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

		log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
		log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
	}

	@Test
	public void test_performRaffle_blackList() {
		RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
				.userId("user001")
				.strategyId(100001L)
				.build();

		RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

		log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
		log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
	}
}
