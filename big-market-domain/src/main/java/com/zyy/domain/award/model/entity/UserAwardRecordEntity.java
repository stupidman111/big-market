package com.zyy.domain.award.model.entity;

import com.zyy.domain.award.model.valobj.AwardStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordEntity {

	/** 用户 ID **/
	private String userId;
	/** 活动 ID **/
	private Long activityId;
	/** 抽奖策略 ID **/
	private Long strategyId;
	/** 抽奖订单 ID **/
	private String orderId;
	/** 奖品 ID **/
	private Integer awardId;
	/** 奖品标题（名称）**/
	private String awardTitle;
	/** 中奖时间 **/
	private Date awardTime;
	/** 奖品状态（create-创建，completed-发奖完成 **/
	private AwardStateVO awardState;
}
