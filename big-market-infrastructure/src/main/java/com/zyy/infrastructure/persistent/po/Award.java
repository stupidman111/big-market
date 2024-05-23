package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class Award {

	/** 自增ID **/
	private Long id;
	/** 奖品ID **/
	private Integer awardId;
	/** 奖品对接标识 **/
	private String awardKey;
	/** 奖品配置信息 **/
	private String awardConfig;
	/** 奖品描述信息 **/
	private String awardDesc;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
