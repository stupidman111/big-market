package com.zyy.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class UserBehaviorRebateOrder {

	/** 自增 ID **/
	private Long id;
	/** 用户 ID **/
	private String userId;
	/** 订单 ID **/
	private String orderId;
	/** 行为类型（sign-签到，openai_pay-支付） **/
	private String behaviorType;
	/** 返利描述 **/
	private String rebateDesc;
	/** 返利类型（sku活动库存充值商品，integral用户活动积分） **/
	private String rebateType;
	/** 返利配置 【sku值，积分值】**/
	private String rebateConfig;
	/** 业务 ID **/
	private String bizId;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
