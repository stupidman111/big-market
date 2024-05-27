package com.zyy.domain.strategy.model.entity;


import com.zyy.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRuleEntity {

	/** 策略ID **/
	private Long strategyId;
	/** 奖品ID **/
	private Integer awardId;
	/** 规则类型，1：策略规则，2：奖品规则 **/
	private Integer ruleType;
	/** 规则类型，rule_random：随机值，rule_lock：抽奖n次解锁，rule_luck_award：幸运奖，rule_weight：策略权重抽奖 **/
	private String ruleModel;
	/** 规则比值 **/
	private String ruleValue;
	/** 规则描述 **/
	private String ruleDesc;

	//解析规则权重到map中
	public Map<String, List<Integer>> getRuleWeightValues() {
		if (!"rule_weight".equals(this.ruleModel)) return null;
		String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
		Map<String, List<Integer>> ruleValueMap = new HashMap<>();
		for (String ruleValueGroup : ruleValueGroups) {
			if (ruleValueGroup == null || ruleValueGroup.isEmpty()) {
				return ruleValueMap;
			}

			String[] parts = ruleValueGroup.split(Constants.COLON);

			if (parts.length != 2) {
				throw new IllegalArgumentException("rule_weight invalid input format" + ruleValueGroup);
			}

			String[] valueStrings = parts[1].split(Constants.SPLIT);
			List<Integer> values = new ArrayList<>();
			for (String valueString : valueStrings) {
				values.add(Integer.parseInt(valueString));
			}
			ruleValueMap.put(ruleValueGroup, values);
		}

		return ruleValueMap;
	}
}
