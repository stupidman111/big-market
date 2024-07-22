package com.zyy.domain.activity.model.aggregate;

import com.zyy.domain.activity.model.entity.ActivityOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账户额度下单聚合对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuotaOrderAggregate {

	/** 用户 ID **/
	private String userId;
	/** 活动 ID **/
	private Long activityId;
	/** 总次数 **/
	private Integer totalCount;
	/** 日次数 **/
	private Integer dayCount;
	/** 月次数 **/
	private Integer monthCount;
	/** 活动订单实体 **/
	private ActivityOrderEntity activityOrderEntity;
}
