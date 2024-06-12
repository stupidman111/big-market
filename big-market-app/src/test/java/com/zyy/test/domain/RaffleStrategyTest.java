package com.zyy.test.domain;


import com.alibaba.fastjson.JSON;
import com.zyy.domain.strategy.model.entity.RaffleAwardEntity;
import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;
import com.zyy.domain.strategy.service.IRaffleStrategy;
import com.zyy.domain.strategy.service.armory.IStrategyArmory;
import com.zyy.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.zyy.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import com.zyy.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
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
	private IStrategyArmory strategyArmory;

	@Resource
	private IRaffleStrategy raffleStrategy;

	@Resource
	private RuleWeightLogicChain ruleWeightLogicChain;

	@Before
	public void setUp() {
		// 策略装配 100001、100002、100003
		log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100001L));
		log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100006L));
		log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100003L));

		// 通过反射 mock 规则中的值
		ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 3500L);
	}

	@Test
	public void test_performRaffle() {
		RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
				.userId("zy")
				.strategyId(100006L)
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

	@Test
	public void test_performRaffle_lock() {
		RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
				.userId("zy")
				.strategyId(100003L)
				.build();

		RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
		log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
		log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
	}
}
