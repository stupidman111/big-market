package com.zyy.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 活动商品充值实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuRechargeEntity {
	/** 用户 ID **/
	private String userId;
	/** SKU （activityId + activityCount） **/
	private Long sku;
	/** 外部业务单号（用于做幂等操作-多次调用结果能确保结果唯一）**/
	private String outBusinessNo;
}
