package com.zyy.domain.activity.repository;

import com.zyy.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.zyy.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.zyy.domain.activity.model.entity.*;
import com.zyy.domain.activity.model.valobj.ActivitySkuStockKeyVO;

import java.util.Date;
import java.util.List;

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
	 * @param createPartakeOrderAggregate
	 */
	void doSaveOrder(CreateQuotaOrderAggregate createPartakeOrderAggregate);

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

	/**
	 * 查询未被使用的抽奖订单
	 * @param partakeRaffleActivityEntity
	 * @return
	 */
	UserRaffleOrderEntity queryNoUsedRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);


	/**
	 * 保存聚合对象
	 * @param createPartakeOrderAggregate
	 */
	void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate);

	/**
	 * 查活动账户
	 * @param userId
	 * @param activityId
	 * @return
	 */
	ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId);


	/**
	 * 查活动月账户
	 * @param userId
	 * @param activityId
	 * @param month
	 * @return
	 */
	ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month);

	/**
	 *  查活动日账户
	 * @param userId
	 * @param activityId
	 * @param day
	 * @return
	 */
	ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day);

	/**
	 * 根据活动ID 查该活动的所有 SKU
	 * @param activityId
	 * @return
	 */
	List<ActivitySkuEntity> queryActivitySkuListByActivityId(Long activityId);

	/**
	 * 查询指定用户参与指定活动的日次数
	 * @param activityId
	 * @param userId
	 * @return
	 */
	Integer queryRaffleActivityDayPartakeCount(Long activityId, String userId);
}
