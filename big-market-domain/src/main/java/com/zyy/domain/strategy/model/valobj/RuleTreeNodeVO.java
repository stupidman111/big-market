package com.zyy.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeVO {

	/** 所属规则树Id **/
	private String treeId;
	/** 规则Key **/
	private String ruleKey;
	/** 规则描述 **/
	private String ruleDesc;
	/** 规则比值 **/
	private String ruleValue;
	/** 规则连线 **/
	private List<RuleTreeNodeLineVO> treeNodeLineVOList;
}
