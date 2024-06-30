package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.RaffleActivityCount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityCountDao {
	RaffleActivityCount queryRaffleActivityCountByActivityCountId(Long activityCountId);
}
