package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyAwardDao {

	/** 查询所有【策略-奖品】信息 **/
	List<StrategyAward> queryStrategyAwardList();

	List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);
}
