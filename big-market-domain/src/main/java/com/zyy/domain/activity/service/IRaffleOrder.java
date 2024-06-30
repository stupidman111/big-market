package com.zyy.domain.activity.service;


import com.zyy.domain.activity.model.entity.ActivityOrderEntity;
import com.zyy.domain.activity.model.entity.ActivityShopCartEntity;

public interface IRaffleOrder {

	ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}
