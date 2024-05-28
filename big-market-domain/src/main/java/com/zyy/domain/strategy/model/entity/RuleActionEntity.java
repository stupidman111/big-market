package com.zyy.domain.strategy.model.entity;

import com.zyy.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

	private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
	private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
	private String ruleModel;//
	private T data;//

	static public class RaffleEntity {

	}

	/** 抽奖前 **/
	@EqualsAndHashCode(callSuper = true)
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	static public class RaffleBeforeEntity extends RaffleEntity {

		/** 策略ID **/
		private Long strategyId;
		/** 权重值key **/
		private String ruleWeightValueKey;
		/** 奖品ID **/
		private Integer awardId;
	}

	/** 抽奖中 **/
	static public class RaffleAfterEntity extends RaffleEntity {

	}
}
