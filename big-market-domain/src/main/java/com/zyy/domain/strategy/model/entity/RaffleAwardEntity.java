package com.zyy.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardEntity {


	/** 奖品ID **/
	private Integer awardId;

	/** 奖品配置信息 **/
	private String awardConfig;

	/** 奖品顺序号 **/
	private Integer sort;

	//	/** 策略ID **/
//	private Long strategyId;
	//	/** 奖品对接标识 **/
//	private String awardKey;
	//	/** 奖品内容描述 **/
//	private String awardDesc;

}
