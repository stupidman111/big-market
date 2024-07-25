package com.zyy.domain.rebate.model.aggregate;

import com.zyy.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.zyy.domain.rebate.model.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行为返利集合对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorRebateAggregate {

	/** 用户ID **/
	private String userId;
	/** 行为返利订单实体对象 **/
	private BehaviorRebateOrderEntity behaviorRebateOrderEntity;
	/** 任务实体对象 **/
	private TaskEntity taskEntity;
}
