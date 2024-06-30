package com.zyy.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityShopCartEntity {
	/** 用户 ID **/
	private String userId;
	/** SKU （activityId + activityCount) **/
	private Long sku;
}
