package com.zyy.domain.activity.service;

import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.domain.activity.service.rule.factory.DefaultActionChainFactory;

public class RaffleActivitySupport {

	protected DefaultActionChainFactory defaultActivityFactoryChain;

	protected IActivityRepository activityRepository;

	public RaffleActivitySupport(DefaultActionChainFactory defaultActivityFactoryChain,
								 IActivityRepository activityRepository) {
		this.defaultActivityFactoryChain = defaultActivityFactoryChain;
		this.activityRepository = activityRepository;
	}

	/**
	 * 通过 sku 获取 sku实体对象-ActivitySkuEntity
	 * @param sku
	 * @return
	 */
	public ActivitySkuEntity queryActivitySku(Long sku) {
		return activityRepository.queryActivitySku(sku);
	}

	/**
	 * 通过 activityId 获取 活动实体对象-ActivityEntity
	 * @param activityId
	 * @return
	 */
	public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
		return activityRepository.queryRaffleActivityByActivityId(activityId);
	}

	/**
	 * 通过 activityCountId 获取 活动库存 实体对象-ActivityCountEntity
	 * @param activityCountId
	 * @return
	 */
	public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
		return activityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
	}
}
