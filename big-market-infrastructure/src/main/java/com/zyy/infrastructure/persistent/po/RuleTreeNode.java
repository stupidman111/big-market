package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RuleTreeNode {

	/** 自增ID **/
	private Long id;

	/** 所属规则树ID **/
	private String treeId;

	/** 节点规则key **/
	private String ruleKey;

	/** 节点规则描述 **/
	private String ruleDesc;

	/** 节点规则比值 **/
	private String ruleValue;

	/** 创建时间 **/
	private Date createTime;

	/** 更新时间 **/
	private Date updateTime;
}
