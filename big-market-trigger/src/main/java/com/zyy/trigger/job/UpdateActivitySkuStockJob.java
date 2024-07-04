package com.zyy.trigger.job;

import com.zyy.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.zyy.domain.activity.service.ISkuStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class UpdateActivitySkuStockJob {

	@Resource
	private ISkuStock skuStock;

	@Scheduled(cron = "0/5 * * * * ?")
	public void exec() {
		try {
			log.info("定时任务，更新活动 sku 库存【延迟队列获取，降低对数据库的更改频次，不要产生竞争】");
			ActivitySkuStockKeyVO activitySkuStockKeyVO = skuStock.takeQueueValue();
			if (null == activitySkuStockKeyVO) return;
			log.info("定时任务，更新活动 sku 库存 sku:{}, activitySkuStockKeyVO:{}", activitySkuStockKeyVO.getSku(),
					activitySkuStockKeyVO.getActivityId());
			skuStock.updateActivitySkuStock(activitySkuStockKeyVO.getSku());
		} catch (Exception e) {
			log.error("定时任务，更新活动 sku 库存 失败", e);
		}
	}

}
