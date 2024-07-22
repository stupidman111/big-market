package com.zyy.domain.activity.service;

import com.zyy.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.zyy.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * 抽奖活动参与服务
 */
public interface IRaffleActivityPartakeService {

	UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);
}
