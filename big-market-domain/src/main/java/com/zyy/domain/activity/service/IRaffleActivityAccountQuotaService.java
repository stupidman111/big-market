package com.zyy.domain.activity.service;


import com.zyy.domain.activity.model.entity.SkuRechargeEntity;

public interface IRaffleActivityAccountQuotaService {

	String createOrder(SkuRechargeEntity skuRechargeEntity);

	/**
	 * 查询日参与次数
	 * @return
	 */
	Integer queryRaffleActivityDayPartakeCount(Long activityId, String userId);
}
