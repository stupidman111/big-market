package com.zyy.test.infrastructure.activity;

import com.zyy.infrastructure.persistent.dao.IRaffleActivitySkuDao;
import com.zyy.infrastructure.persistent.po.RaffleActivitySku;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivitySkuTest {

	@Resource
	private IRaffleActivitySkuDao raffleActivitySkuDao;

	@Test
	public void test_queryActivitySku() {
		RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(9011L);
		log.info("测试结果：{}", raffleActivitySku);
	}

	@Test
	public void test_updateActivitySkuStock() {
		log.info("测试之前 sku剩余库存：{}", raffleActivitySkuDao.queryActivitySku(9011L).getStockCountSurplus());
		raffleActivitySkuDao.updateActivitySkuStock(9011L);
		log.info("测试之后 sku剩余库存：{}", raffleActivitySkuDao.queryActivitySku(9011L).getStockCountSurplus());
	}

	@Test
	public void test_clearActivitySkuStock() {

		log.info("测试之前 sku剩余库存：{}", raffleActivitySkuDao.queryActivitySku(9011L).getStockCountSurplus());
		raffleActivitySkuDao.clearActivitySkuStock(9011L);
		log.info("测试之后 sku剩余库存：{}", raffleActivitySkuDao.queryActivitySku(9011L).getStockCountSurplus());
	}

}
