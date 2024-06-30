package com.zyy.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.zyy.domain.activity.model.entity.*;
import com.zyy.domain.activity.repository.IActivityRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractRaffleActivity implements IRaffleOrder{

	protected IActivityRepository activityRepository;

	public AbstractRaffleActivity(IActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}
	@Override
	public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {

		//1. 根据 activityShopCartEntity - sku 获取 ActivitySkuEntity
		ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());

		//2. 根据 ActivitySkuEntity 获取 ActivityEntity
		ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());

		//3. 根据 ActivityEntity 获取 ActivityCountEntity
		ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

		log.info("查询结果: sku信息：\n {},\n {},\n {}\n", JSON.toJSONString(activitySkuEntity),
				JSON.toJSONString(activityEntity),
				JSON.toJSONString(activityCountEntity));


		return ActivityOrderEntity.builder().build();
	}
}
