package com.zyy.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeVO {

	/** 规则树ID **/
	private Integer treeId;
	/** 规则树名 **/
	private String treeName;
	/** 规则树描述 **/
	private String treeDesc;
	/** 规则树根节点 **/
	private String treeRootRuleNode;
	/** 规则节点 **/
	private Map<String, RuleTreeNodeVo> treeNodeMap;
}
