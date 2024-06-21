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

}
