package com.zyy.domain.activity.model.entity;

import com.zyy.domain.activity.model.valobj.OrderStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityOrderEntity {
	/** 用户 ID **/
	private String userId;
//	/** sku **/
//	private Long sku;
	/** 活动 ID **/
	private Long activityId;
	/** 活动名称 **/
	private String activityName;
	/** 抽奖策略 ID **/
	private Long strategyId;
	/** 订单 ID **/
	private String orderId;
	/** 下单时间 **/
	private Date orderTime;
	/** 总次数 **/
	private Integer totalCount;
	/** 月次数 **/
	private Integer monthCount;
	/** 日次数 **/
	private Integer dayCount;
	/** 订单状态 **/
	private OrderStateVO state;
}
