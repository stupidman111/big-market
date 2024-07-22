package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.UserRaffleOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserRaffleOrderDao {


	UserRaffleOrder queryNoUsedRaffleOrder(UserRaffleOrder userRaffleOrderReq);

	void insert(UserRaffleOrder build);
}
