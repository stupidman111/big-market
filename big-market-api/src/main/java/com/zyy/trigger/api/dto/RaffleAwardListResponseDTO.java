package com.zyy.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应 【抽奖奖品列表】DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardListResponseDTO {

	/** 奖品 ID **/
	private Integer awardId;
	/** 奖品标题 **/
	private String awardTitle;
	/** 奖品副标题 **/
	private String awardSubtitle;
	/** 奖品排序编号 **/
	private Integer sort;
	/** 奖品次数规则-抽奖 N 次后解锁，未配置则为空 **/
	private Integer awardRuleLockCount;
	/** 奖品是否解锁 **/
	private Boolean isAwardUnlock;
	/** 等待解锁次数-规定的抽奖 N 次解锁减去用户已经抽奖次数 **/
	private Integer waitUnLockCount;

}
