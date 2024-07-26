package com.zyy.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返利类型：sku、integral
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVO {

	SKU("sku", "活动库存充值商品"),
	INTEGRAL("integral", "用户活动积分"),
	;

	private final String code;
	private final String info;
}
