package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.RaffleActivityOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityOrderDao {
	void insert(RaffleActivityOrder raffleActivityOrder);
}
