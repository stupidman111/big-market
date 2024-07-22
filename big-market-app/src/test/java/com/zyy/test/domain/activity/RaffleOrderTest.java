package com.zyy.test.domain.activity;

import com.zyy.domain.activity.model.entity.SkuRechargeEntity;
import com.zyy.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.zyy.domain.activity.service.armory.IActivityArmory;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleOrderTest {

	@Resource
	private IRaffleActivityAccountQuotaService raffleOrder;

	@Resource
	private IActivityArmory activityArmory;

	@Resource
	private RedissonClient redissonClient;

	@Before
	public void setUp() {
		redissonClient.getKeys().flushall();
		log.info("装配活动: {}", activityArmory.assemblyActivitySku(9011L));
	}

	@Test
	public void test_createRaffleActivityOrder() {
		SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
		skuRechargeEntity.setUserId("zy");
		skuRechargeEntity.setSku(9011L);
		// outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
		skuRechargeEntity.setOutBusinessNo("383240888158");
		String orderId = raffleOrder.createOrder(skuRechargeEntity);
		log.info("测试结果：{}", orderId);
	}

	/**
	 * 测试库存消耗和最终一致更新
	 * 1. raffle_activity_sku 库表库存可以设置20个
	 * 2. 清空 redis 缓存 flushall
	 * 3. for 循环20次，消耗完库存，最终数据库剩余库存为0
	 */
	@Test
	public void test_createSkuRechargeOrder() throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			try {
				log.info("第 {} 次领取活动", i+1);
				SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
				skuRechargeEntity.setUserId("xiaofuge");
				skuRechargeEntity.setSku(9011L);
				// outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
				skuRechargeEntity.setOutBusinessNo(RandomStringUtils.randomNumeric(12));
				String orderId = raffleOrder.createOrder(skuRechargeEntity);
				log.info("测试结果：{}", orderId);
			} catch (AppException e) {
				log.warn(e.getInfo());
			}
		}

		new CountDownLatch(1).await();
	}
}
