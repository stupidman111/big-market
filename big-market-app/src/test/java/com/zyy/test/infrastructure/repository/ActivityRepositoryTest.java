package com.zyy.test.infrastructure.repository;

import com.zyy.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.zyy.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.zyy.domain.activity.model.entity.ActivityOrderEntity;
import com.zyy.domain.activity.model.valobj.OrderStateVO;
import com.zyy.domain.activity.repository.IActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityRepositoryTest {

	@Resource
	private IActivityRepository activityRepository;

	@Test
	public void test_queryRaffleActivityCountByActivityCountId() {
		//activityRepository.queryRaffleActivityCountByActivityCountId();
	}

	/**
	 * userId activityId totalCount monthCount dayCount activityOrderEntity
	 * userId sku activityId activityName strategyId orderId orderTime totalCount monthCount dayCount state outBusinessNO
	 */
	@Test
	public void test_doSaveOrder() {
		ActivityOrderEntity activityOrderEntity = new ActivityOrderEntity();
		activityOrderEntity.setUserId("zy");
		activityOrderEntity.setSku(9033L);
		activityOrderEntity.setActivityId(100301L);
		activityOrderEntity.setActivityName("测试活动");
		activityOrderEntity.setStrategyId(100006L);
		activityOrderEntity.setOrderId("621885906967");
		activityOrderEntity.setOrderTime(new Date());
		activityOrderEntity.setTotalCount(21);
		activityOrderEntity.setMonthCount(21);
		activityOrderEntity.setDayCount(21);
		activityOrderEntity.setState(OrderStateVO.completed);
		activityOrderEntity.setOutBusinessNO("jd_001");


		CreateQuotaOrderAggregate createQuotaOrderAggregate = new CreateQuotaOrderAggregate();
		createQuotaOrderAggregate.setUserId("zy");
		createQuotaOrderAggregate.setActivityId(100301L);
		createQuotaOrderAggregate.setTotalCount(1);
		createQuotaOrderAggregate.setMonthCount(1);
		createQuotaOrderAggregate.setDayCount(1);
		createQuotaOrderAggregate.setActivityOrderEntity(activityOrderEntity);

		activityRepository.doSaveOrder(createQuotaOrderAggregate);
	}
}
