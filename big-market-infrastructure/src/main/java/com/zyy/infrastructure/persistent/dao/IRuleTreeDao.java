package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRuleTreeDao {

	RuleTree queryRuleTreeByTreeId(String treeId);
}
