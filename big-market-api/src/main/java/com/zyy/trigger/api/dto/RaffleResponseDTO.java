package com.zyy.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应 【抽奖应答结果】DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleResponseDTO {

	/** 奖品 ID **/
	private Integer awardId;
	/** 排序编号 **/
	private Integer awardIndex;
}
