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
	/** 策略奖品库存 **/
	private Integer awardCount;
	/** 策略奖品剩余库存 **/
	private Integer awardCountSurplus;
	/** 策略奖品中奖概率 **/
	private BigDecimal awardRate;
}
