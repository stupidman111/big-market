package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyDao {

	/** 查询所有策略信息 **/
	List<Strategy> queryStrategyList();
}
