package com.zyy.domain.rebate.repository;

import com.zyy.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.zyy.domain.rebate.model.valobj.BehaviorTypeVO;
import com.zyy.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.List;

public interface IBehaviorRebateRepository {

	List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO);

	void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates);
}
