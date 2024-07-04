package com.zyy.domain.activity.service.armory;

import java.util.Date;

public interface IActivityDispatch {

	/**
	 * 根据 sku 扣减奖品缓存库存
	 * @param sku
	 * @param endDateTime 活动结束时间，根据结束时间设置加锁的 key的过期时间（decr分段锁扣减，加锁时使用活动结束时间+1作为过期时间）
	 * @return
	 */
	boolean subtractionActivitySkuStock(Long sku, Date endDateTime);
}
