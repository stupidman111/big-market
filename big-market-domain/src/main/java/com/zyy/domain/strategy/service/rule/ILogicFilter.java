package com.zyy.domain.strategy.service.rule;

import com.zyy.domain.strategy.model.entity.RuleActionEntity;
import com.zyy.domain.strategy.model.entity.RuleMatterEntity;

public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

	RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
