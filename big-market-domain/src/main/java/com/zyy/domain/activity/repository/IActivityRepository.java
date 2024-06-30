package com.zyy.domain.activity.repository;

import com.zyy.domain.activity.model.aggregate.CreateOrderAggregate;
import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;

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
}
