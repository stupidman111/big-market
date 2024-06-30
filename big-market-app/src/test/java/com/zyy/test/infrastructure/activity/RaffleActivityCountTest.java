package com.zyy.test.infrastructure.activity;

import com.zyy.infrastructure.persistent.dao.IRaffleActivityCountDao;
import com.zyy.infrastructure.persistent.po.RaffleActivityCount;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivityCountTest {

	@Resource
	private IRaffleActivityCountDao raffleActivityCountDao;

	@Test
	public void test_queryRaffleActivityCountByActivityCountId() {
		RaffleActivityCount raffleActivityCount = raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(1L);
		log.info("测试结果：{}", raffleActivityCount);
	}

}
