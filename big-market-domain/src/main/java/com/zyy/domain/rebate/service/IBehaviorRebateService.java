package com.zyy.domain.rebate.service;

import com.zyy.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;


public interface IBehaviorRebateService {

	/**
	 * 创建行为动作的入账订单
	 * @param behaviorEntity
	 * @return
	 */
	List<String> createOrder(BehaviorEntity behaviorEntity);
}
