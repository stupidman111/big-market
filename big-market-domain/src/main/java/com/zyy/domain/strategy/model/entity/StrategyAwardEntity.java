package com.zyy.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 【策略-奖品】实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardEntity {

	/** 抽奖策略ID **/
	private Long strategyId;
	/** 策略奖品ID **/
	private Integer awardId;
	/** 抽奖奖品标题 **/
	private String awardTitle;
	/** 抽奖奖品副标题 **/
	private String awardSubtitle;
	/** 策略奖品库存 **/
	private Integer awardCount;
	/** 策略奖品剩余库存 **/
	private Integer awardCountSurplus;
	/** 策略奖品中奖概率 **/
	private BigDecimal awardRate;
	/** 抽奖奖品顺序 **/
	private Integer sort;
}
