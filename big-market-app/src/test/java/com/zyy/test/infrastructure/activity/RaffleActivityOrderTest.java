package com.zyy.test.infrastructure.activity;

import com.zyy.infrastructure.persistent.dao.IRaffleActivityOrderDao;
import com.zyy.infrastructure.persistent.po.RaffleActivityOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivityOrderTest {

	@Resource
	private IRaffleActivityOrderDao raffleActivityOrderDao;

	@Test
	public void test_insert() {
		RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
		raffleActivityOrder.setUserId("zy");
		raffleActivityOrder.setSku(9012L);
		raffleActivityOrder.setActivityId(100301L);
		raffleActivityOrder.setActivityName("zy测试活动");
		raffleActivityOrder.setStrategyId(100006L);
		raffleActivityOrder.setOrderId(RandomStringUtils.randomNumeric(12));
		raffleActivityOrder.setOrderTime(new Date());
		raffleActivityOrder.setState("not_used");
		raffleActivityOrder.setTotalCount(10);
		raffleActivityOrder.setMonthCount(10);
		raffleActivityOrder.setDayCount(10);
		raffleActivityOrder.setOutBusinessNo("jd_001");
		// 插入数据
		raffleActivityOrderDao.insert(raffleActivityOrder);
	}
}
