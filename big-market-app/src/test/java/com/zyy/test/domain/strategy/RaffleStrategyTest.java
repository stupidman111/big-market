package com.zyy.test.domain.strategy;


import com.alibaba.fastjson.JSON;
import com.zyy.domain.strategy.model.entity.RaffleAwardEntity;
import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;
import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.zyy.domain.strategy.service.IRaffleAward;
import com.zyy.domain.strategy.service.IRaffleStock;
import com.zyy.domain.strategy.service.IRaffleStrategy;
import com.zyy.domain.strategy.service.armory.IStrategyArmory;
import com.zyy.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import com.zyy.domain.strategy.service.rule.tree.impl.RuleLockLogicTreeNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

	@Resource
	private RuleLockLogicTreeNode ruleLockLogicTreeNode;

	@Resource
	private IRaffleStock raffleStock;

	@Before
	public void setUp() {
		// 策略装配 100001、100002、100003
		//log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100001L));
		log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100006L));
		//log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100003L));

		// 通过反射 mock 规则中的值
		ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 4900L);
		ReflectionTestUtils.setField(ruleLockLogicTreeNode, "userRaffleCount", 10L);
	}

	@Test
	public void test_performRaffle() throws InterruptedException {
		for (int i = 0; i < 3; i++) {
			RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
					.userId("zy")
					.strategyId(100006L)
					.build();

 			RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

			log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
			log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
		}

		new CountDownLatch(1).await();
	}

	@Test
	public void test_takeQueueValue() throws InterruptedException {
		StrategyAwardStockKeyVO strategyAwardStockKeyVO = raffleStock.takeQueueValue();
		log.info("测试结果：{}", JSON.toJSONString(strategyAwardStockKeyVO));
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

	@Resource
	private IRaffleAward raffleAward;

	@Test
	public void test_queryRaffleStrategyAwardList() {
		List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryRaffleStrategyAwardList(100006L);
		for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
			log.info("ans: {}\n", strategyAwardEntity);
		}
	}
}
