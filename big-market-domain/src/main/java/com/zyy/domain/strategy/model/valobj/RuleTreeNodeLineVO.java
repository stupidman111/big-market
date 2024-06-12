package com.zyy.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeLineVO {

	/** 所属规则树ID **/
	private String treeId;
	/** 规则key节点 FROM **/
	private String ruleNodeFrom;
	/** 规则key节点 TO **/
	private String ruleNodeTo;
	/** 规则限定类型（分支判断）: **/
	private RuleLimitTypeVO ruleLimitType;
	/** 限定值 **/
	private RuleLogicCheckTypeVO ruleLimitValue;

}
