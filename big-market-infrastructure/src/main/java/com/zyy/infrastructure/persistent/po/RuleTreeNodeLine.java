package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RuleTreeNodeLine {

	/** 自增ID **/
	private Long id;

	/** 所属规则树ID **/
	private String treeId;

	/** 规则KEY节点：FROM **/
	private String ruleNodeFrom;

	/** 规则KEY节点：TO **/
	private String ruleNodeTo;

	/** 限定类型 **/
	private String ruleLimitType;

	/** 限定值 **/
	private String ruleLimitValue;

	/** 创建时间 **/
	private Date createTime;

	/** 更新时间 **/
	private Date updateTime;
}
