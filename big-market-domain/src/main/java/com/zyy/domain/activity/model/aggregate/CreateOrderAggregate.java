package com.zyy.domain.activity.model.aggregate;

import com.zyy.domain.activity.model.entity.ActivityAccountEntity;
import com.zyy.domain.activity.model.entity.ActivityOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {

	/** 活动账户实体 **/
	private ActivityAccountEntity activityAccountEntity;

	/** 活动订单实体 **/
	private ActivityOrderEntity activityOrderEntity;
}
