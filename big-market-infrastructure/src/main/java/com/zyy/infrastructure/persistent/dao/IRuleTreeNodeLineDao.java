package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.RuleTreeNodeLine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRuleTreeNodeLineDao {

	List<RuleTreeNodeLine> queryRuleTreeNodeLineListByTreeId(String treeId);
}
