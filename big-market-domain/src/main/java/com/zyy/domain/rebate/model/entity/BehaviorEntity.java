package com.zyy.domain.rebate.model.entity;


import com.zyy.domain.rebate.model.valobj.BehaviorTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorEntity {

	/** 用户 ID **/
	private String userId;
	/** 行为类型：sign-签到，openai_pay-支付 **/
	private BehaviorTypeVO behaviorTypeVO;
	/** 业务 ID **/
	private String outBusinessNo;
}
