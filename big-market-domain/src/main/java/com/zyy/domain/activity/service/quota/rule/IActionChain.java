package com.zyy.domain.activity.service.quota.rule;

import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;

public interface IActionChain extends IActionChainArmory{

	boolean action(ActivitySkuEntity activitySkuEntity,
				   ActivityEntity activityEntity,
				   ActivityCountEntity activityCountEntity);
}
