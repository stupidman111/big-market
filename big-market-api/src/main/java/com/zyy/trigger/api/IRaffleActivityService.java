package com.zyy.trigger.api;

import com.zyy.trigger.api.dto.ActivityDrawRequestDTO;
import com.zyy.trigger.api.dto.ActivityDrawResponseDTO;
import com.zyy.types.model.Response;

/**
 * 抽奖活动服务接口
 */
public interface IRaffleActivityService {

	/**
	 * 活动装配，数据预热
	 * @param activityId
	 * @return
	 */
	Response<Boolean> armory(Long activityId);

	/**
	 * 活动抽奖接口
	 */
	Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);
}
