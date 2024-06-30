package com.zyy.test.infrastructure.activity;

import com.alibaba.fastjson.JSON;
import com.zyy.infrastructure.persistent.dao.IRaffleActivityDao;
import com.zyy.infrastructure.persistent.po.RaffleActivity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivityTest {

	@Resource
	private IRaffleActivityDao raffleActivityDao;

	@Test
	public void test_queryRaffleActivityByActivityId() {
		RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(100301L);

		log.info("查询结果: {}", JSON.toJSONString(raffleActivity));
	}


}
