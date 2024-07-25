package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.UserBehaviorRebateOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserBehaviorRebateOrderDao {

	void insert(UserBehaviorRebateOrder userBehaviorRebateOrder);

}
