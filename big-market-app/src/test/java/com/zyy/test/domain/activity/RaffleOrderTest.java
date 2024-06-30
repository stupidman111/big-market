package com.zyy.test.domain.activity;

import com.alibaba.fastjson.JSON;
import com.zyy.domain.activity.model.entity.ActivityOrderEntity;
import com.zyy.domain.activity.model.entity.SkuRechargeEntity;
import com.zyy.domain.activity.service.IRaffleOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleOrderTest {

	@Resource
	private IRaffleOrder raffleOrder;

	@Test
	public void test_createRaffleActivityOrder() {
		SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
		skuRechargeEntity.setUserId("zy");
		skuRechargeEntity.setSku(9011L);
		// outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
		skuRechargeEntity.setOutBusinessNo("383240888158");
		String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
		log.info("测试结果：{}", orderId);
	}
}
