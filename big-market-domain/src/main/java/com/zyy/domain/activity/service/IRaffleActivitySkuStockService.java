package com.zyy.domain.activity.service;

import com.zyy.domain.activity.model.valobj.ActivitySkuStockKeyVO;

public interface IRaffleActivitySkuStockService {

	/**
	 * 获取sku库存消耗队列
	 * @return
	 * @throws InterruptedException
	 */
	ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException;

	/**
	 * 情况队列
	 */
	void clearQueueValue();

	/**
	 * 延迟队列 + 任务趋势更新活动sku库存（-1）
	 * @param sku
	 */
	void updateActivitySkuStock(Long sku);

	/**
	 * 缓存库存已经消耗完毕，清空数据库库存
	 * @param sku
	 */
	void clearActivitySkuStock(Long sku);
}
