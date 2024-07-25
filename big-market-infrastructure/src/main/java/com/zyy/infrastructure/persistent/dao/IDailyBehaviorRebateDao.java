package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.DailyBehaviorRebate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDailyBehaviorRebateDao {
	List<DailyBehaviorRebate> queryDailyBehaviorRebateByBehaviorType(String code);
}
