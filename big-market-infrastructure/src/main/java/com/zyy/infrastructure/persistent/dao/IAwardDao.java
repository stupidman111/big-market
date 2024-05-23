package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IAwardDao {

	/** 查询所有奖品信息 **/
	List<Award> queryAwardList();


}
