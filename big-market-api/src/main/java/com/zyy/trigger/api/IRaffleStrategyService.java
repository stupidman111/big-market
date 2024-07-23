package com.zyy.trigger.api;

import com.zyy.trigger.api.dto.RaffleAwardListRequestDTO;
import com.zyy.trigger.api.dto.RaffleAwardListResponseDTO;
import com.zyy.trigger.api.dto.RaffleRequestDTO;
import com.zyy.trigger.api.dto.RaffleResponseDTO;
import com.zyy.types.model.Response;

import java.util.List;

/**
 * 抽奖策略服务接口
 */
public interface IRaffleStrategyService {

	Response<Boolean> strategyArmory(Long strategyId);

	Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);

	Response<RaffleResponseDTO> randomRaffle(RaffleRequestDTO requestDTO);

}
