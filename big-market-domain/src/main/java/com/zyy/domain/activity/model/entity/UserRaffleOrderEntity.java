package com.zyy.domain.activity.model.entity;

import com.zyy.domain.activity.model.valobj.UserRaffleOrderStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户抽奖订单实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRaffleOrderEntity {

	private String userId;
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
	/** 订单状态 **/
	private UserRaffleOrderStateVO orderState;
	/** 创建时间 **/

}
