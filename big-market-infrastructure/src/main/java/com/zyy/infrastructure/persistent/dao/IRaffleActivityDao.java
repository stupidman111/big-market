package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.RaffleActivity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityDao {
	RaffleActivity queryRaffleActivityByActivityId(Long activityId);
}
