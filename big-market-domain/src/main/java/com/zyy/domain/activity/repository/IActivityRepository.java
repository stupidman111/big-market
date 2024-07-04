package com.zyy.domain.activity.repository;

import com.zyy.domain.activity.model.aggregate.CreateOrderAggregate;
import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;
import com.zyy.domain.activity.model.valobj.ActivitySkuStockKeyVO;

import java.util.Date;

public interface IActivityRepository {

	/**
	 * 根据 sku 获取 ActivitySkuEntity
	 * @param sku
	 * @return
	 */
	ActivitySkuEntity queryActivitySku(Long sku);

	/**
	 * 根据 activityId 获取 ActivityEntity
 	 * @param activityId
	 * @return
	 */
	ActivityEntity queryRaffleActivityByActivityId(Long activityId);

	/**
	 * 根据 activityCountId 获取 ActivityCountId
	 * @param activityCountId
	 * @return
	 */
	ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

	/**
	 * 处理订单
	 * @param createOrderAggregate
	 */
	void doSaveOrder(CreateOrderAggregate createOrderAggregate);

	/**
	 * 预热 sku 缓存
	 * @param cacheKey
	 * @param stockCount
	 */
	void cacheActivitySkuStockCount(String cacheKey, Integer stockCount);

	/**
	 * 扣减 sku 库存（需要检查过期时间）
	 * @param sku
	 * @param cacheKey
	 * @param endDateTime
	 * @return
	 */
	boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime);

	/**
	 * 发送消息到延迟队列
	 * @param message
	 */
	void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO message);

	/**
	 * 从延迟队列获取消息
	 * @return
	 */
	ActivitySkuStockKeyVO takeQueueValue();

	/**
	 * 清空延迟队列
	 */
	void clearQueueValue();

	/**
	 * 更新 sku 库存
	 * @param sku
	 */
	void updateActivitySkuStock(Long sku);

	/**
	 * 清空数据库sku库存
	 * @param sku
	 */
	void clearActivitySkuStock(Long sku);
}
