package com.zyy.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardStockKeyVO{

	/** 策略ID **/
	private Long strategyId;
	/** 奖品ID **/
	private Integer awardId;

}
