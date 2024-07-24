package com.zyy.trigger.http;

import com.alibaba.fastjson.JSON;
import com.zyy.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.zyy.domain.activity.service.quota.RaffleActivityAccountQuotaService;
import com.zyy.domain.strategy.model.entity.RaffleAwardEntity;
import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;
import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.domain.strategy.service.IRaffleAward;
import com.zyy.domain.strategy.service.IRaffleRule;
import com.zyy.domain.strategy.service.IRaffleStrategy;
import com.zyy.domain.strategy.service.armory.IStrategyArmory;
import com.zyy.trigger.api.IRaffleStrategyService;
import com.zyy.trigger.api.dto.RaffleAwardListRequestDTO;
import com.zyy.trigger.api.dto.RaffleAwardListResponseDTO;
import com.zyy.trigger.api.dto.RaffleRequestDTO;
import com.zyy.trigger.api.dto.RaffleResponseDTO;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import com.zyy.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/strategy/")
public class RaffleStrategyController implements IRaffleStrategyService {

	@Resource
	private IRaffleAward raffleAward;

	@Resource
	private IRaffleStrategy raffleStrategy;

	@Resource
	private IStrategyArmory strategyArmory;

	@Resource
	private IRaffleRule raffleRule;

	@Resource
	private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

	/**
	 * 处理【策略装配】请求
	 * @param strategyId
	 * @return
	 */
	@RequestMapping(value = "strategy_armory", method = RequestMethod.GET)
	@Override
	public Response<Boolean> strategyArmory(@RequestParam Long strategyId) {

		try {
			log.info("抽奖策略装配开始 strategyId: {}", strategyId);
			boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);
			Response<Boolean> response = Response.<Boolean>builder()
					.code(ResponseCode.SUCCESS.getCode())
					.info(ResponseCode.SUCCESS.getInfo())
					.data(armoryStatus)
					.build();
			log.info("抽奖策略装配完成 strategyId: {}, response: {}", strategyId, JSON.toJSONString(response));
			return response;
		} catch (Exception e) {
			log.error("抽奖策略装配失败 strategyId: {}, Exception: {}", strategyId, e);
			return Response.<Boolean>builder()
					.code(ResponseCode.UN_ERROR.getCode())
					.info(ResponseCode.UN_ERROR.getInfo())
					.build();
		}
	}

	/**
	 * 处理【获取奖品列表】请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "query_raffle_award_list", method = RequestMethod.POST)
	@Override
	public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO request) {

		try {
			log.info("查询抽奖奖品列表配开始 userId:{} activityId：{}", request.getUserId(), request.getActivityId());
			//1.参数校验
			if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
				throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
			}

 			//2.查询奖品配置
			List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryRaffleStrategyAwardListByActivityId(request.getActivityId());

			//3.获取规则配置
			String[] treeIds = strategyAwardEntities.stream()
					.map(StrategyAwardEntity::getRuleModels)
					.filter(ruleModel -> ruleModel != null && !ruleModel.isEmpty())
					.toArray(String[]::new);

			//4.查询规则配置-获取奖品的解锁限制，抽奖 N 次后解锁
			Map<String, Integer> ruleLockCountMap = raffleRule.queryAwardRuleLockCount(treeIds);

			//5.查询抽奖次数-用户已经参与的抽奖次数
			Integer dayPartakeCount = raffleActivityAccountQuotaService.queryRaffleActivityDayPartakeCount(request.getActivityId(), request.getUserId());

			//6.遍历填充数据
			List<RaffleAwardListResponseDTO> raffleAwardListResponseDTOS =  new ArrayList<>(strategyAwardEntities.size());
			for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
				Integer awardRuleLockCount = ruleLockCountMap.get(strategyAward.getRuleModels());

				raffleAwardListResponseDTOS.add(RaffleAwardListResponseDTO.builder()
						.awardId(strategyAward.getAwardId())
						.awardTitle(strategyAward.getAwardTitle())
						.awardSubtitle(strategyAward.getAwardSubtitle())
						.sort(strategyAward.getSort())
						.awardRuleLockCount(awardRuleLockCount)
						.isAwardUnlock(null == awardRuleLockCount || dayPartakeCount >= awardRuleLockCount)
						.waitUnLockCount(null == awardRuleLockCount || awardRuleLockCount <= dayPartakeCount ? 0 : awardRuleLockCount - dayPartakeCount)
						.build());
			}

			Response<List<RaffleAwardListResponseDTO>> response = Response.<List<RaffleAwardListResponseDTO>>builder()
					.code(ResponseCode.SUCCESS.getCode())
					.info(ResponseCode.SUCCESS.getInfo())
					.data(raffleAwardListResponseDTOS)
					.build();

			log.info("查询抽奖奖品列表配置完成 userId:{} activityId：{} response: {}", request.getUserId(), request.getActivityId(), JSON.toJSONString(response));
			// 返回结果
			return response;
		} catch (Exception e) {
			log.error("查询抽奖奖品列表配置失败 userId:{} activityId：{}", request.getUserId(), request.getActivityId(), e);
			return Response.<List<RaffleAwardListResponseDTO>>builder()
					.code(ResponseCode.UN_ERROR.getCode())
					.info(ResponseCode.UN_ERROR.getInfo())
					.build();
		}
	}

	/**
	 * 处理【随机抽奖】请求
	 * @param requestDTO
	 * @return
	 */
	@RequestMapping(value = "random_raffle", method = RequestMethod.POST)
	@Override
	public Response<RaffleResponseDTO> randomRaffle(@RequestBody RaffleRequestDTO requestDTO) {

		try {
			log.info("随机抽奖开始：strategy: {}");
			RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
					.userId("system")
					.strategyId(requestDTO.getStrategyId())
					.build();
			RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
			Response<RaffleResponseDTO> response = Response.<RaffleResponseDTO>builder()
					.code(ResponseCode.SUCCESS.getCode())
					.info(ResponseCode.SUCCESS.getInfo())
					.data(RaffleResponseDTO.builder()
							.awardId(raffleAwardEntity.getAwardId())
							.awardIndex(raffleAwardEntity.getSort())
							.build())
					.build();
			log.info("随机抽奖完成：strategyId: {}, response: {}", requestDTO.getStrategyId(), JSON.toJSONString(response));
			return response;
		} catch (AppException e) {
			log.info("随机抽奖失败：strategyId: {}, error: {}", requestDTO.getStrategyId(), e);
			return Response.<RaffleResponseDTO>builder()
					.code(e.getCode())
					.info(e.getInfo())
					.build();
		} catch (Exception e) {
			log.info("随机抽奖失败：strategyId: {}, error: {}", requestDTO.getStrategyId(), e);
			return Response.<RaffleResponseDTO>builder()
					.code(ResponseCode.UN_ERROR.getCode())
					.info(ResponseCode.UN_ERROR.getInfo())
					.build();
		}

	}
}
