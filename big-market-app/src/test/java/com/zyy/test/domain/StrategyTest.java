package com.zyy.test.domain;

import com.zyy.domain.strategy.service.armory.IStrategyArmory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {

	@Resource
	private IStrategyArmory strategyArmory;

	@Test
	public void test_strategyArmory() {
		boolean success = strategyArmory.assembleLotteryStrategy(100001L);
		log.info("测试结果：{}", success);
	}

	@Test
	public void test_getAssembleRandomVal() {
		log.info("测试结果：{} - 奖品ID值", strategyArmory.getRandomAwardId(100001L));
	}
}
