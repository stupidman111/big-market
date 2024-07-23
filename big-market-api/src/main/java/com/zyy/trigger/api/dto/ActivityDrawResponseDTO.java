package com.zyy.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 活动抽奖响应对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDrawResponseDTO {

	/** 奖品 ID **/
	private Integer awardId;
	/** 奖品标题 **/
	private String awardTitle;
	/** 排序编号 **/
	private Integer awardIndex;
}
