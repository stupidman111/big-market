package com.zyy.trigger.http;

import com.zyy.domain.activity.model.entity.UserRaffleOrderEntity;
import com.zyy.domain.activity.service.IRaffleActivityPartakeService;
import com.zyy.domain.activity.service.armory.IActivityArmory;
import com.zyy.domain.award.model.entity.UserAwardRecordEntity;
import com.zyy.domain.award.model.valobj.AwardStateVO;
import com.zyy.domain.award.service.IAwardService;
import com.zyy.domain.strategy.model.entity.RaffleAwardEntity;
import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;
import com.zyy.domain.strategy.service.IRaffleStrategy;
import com.zyy.domain.strategy.service.armory.IStrategyArmory;
import com.zyy.trigger.api.IRaffleActivityService;
import com.zyy.trigger.api.dto.ActivityDrawRequestDTO;
import com.zyy.trigger.api.dto.ActivityDrawResponseDTO;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import com.zyy.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/activity/")
public class RaffleActivityController implements IRaffleActivityService  {

	@Resource
	private IRaffleActivityPartakeService raffleActivityPartakeService;
	@Resource
	private IRaffleStrategy raffleStrategy;
	@Resource
	private IAwardService awardService;
	@Resource
	private IActivityArmory activityArmory;
	@Resource
	private IStrategyArmory strategyArmory;


	/**
	 * 活动预热
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "armory", method = RequestMethod.GET)
	@Override
	public Response<Boolean> armory(@RequestParam Long activityId) {
		try {
			log.info("活动装配，数据预热，开始 activityId:{}", activityId);
			// 1. 活动装配
			activityArmory.assembleActivitySkuByActivityId(activityId);
			// 2. 策略装配
			strategyArmory.assembleLotteryStrategyByActivityId(activityId);
			Response<Boolean> response = Response.<Boolean>builder()
					.code(ResponseCode.SUCCESS.getCode())
					.info(ResponseCode.SUCCESS.getInfo())
					.data(true)
					.build();
			log.info("活动装配，数据预热，完成 activityId:{}", activityId);
			return response;
		} catch (Exception e) {
			log.error("活动装配，数据预热，失败 activityId:{}", activityId, e);
			return Response.<Boolean>builder()
					.code(ResponseCode.UN_ERROR.getCode())
					.info(ResponseCode.UN_ERROR.getInfo())
					.build();
		}
	}

	/**
	 * 抽奖接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "draw", method = RequestMethod.POST)
	@Override
	public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
		try {
			log.info("活动抽奖 userId:{} activityId:{}", request.getUserId(), request.getActivityId());

			//1. 参数校验
			if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
				throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
			}

			//2. 参与活动（创建参与订单）
			UserRaffleOrderEntity orderEntity = raffleActivityPartakeService.createOrder(request.getUserId(), request.getActivityId());
			log.info("活动抽奖，创建订单 userId:{} activityId:{} orderId:{}", request.getUserId(), request.getActivityId(), orderEntity.getOrderId());

			//3. 抽奖策略（执行抽奖）
			RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
					.userId(orderEntity.getUserId())
					.strategyId(orderEntity.getStrategyId())
					.endDateTime(orderEntity.getEndDateTime())
					.build());

			//4. 存放结果（写入中奖记录）
			UserAwardRecordEntity userAwardRecord = UserAwardRecordEntity.builder()
					.userId(orderEntity.getUserId())
					.activityId(orderEntity.getActivityId())
					.strategyId(orderEntity.getStrategyId())
					.orderId(orderEntity.getOrderId())
					.awardId(raffleAwardEntity.getAwardId())
					.awardTitle(raffleAwardEntity.getAwardTitle())
					.awardTime(new Date())
					.awardState(AwardStateVO.create)
					.build();
			awardService.saveUserAwardRecord(userAwardRecord);

			//5. 返回结果
			return Response.<ActivityDrawResponseDTO>builder()
					.code(ResponseCode.SUCCESS.getCode())
					.info(ResponseCode.SUCCESS.getInfo())
					.data(ActivityDrawResponseDTO.builder()
							.awardId(raffleAwardEntity.getAwardId())
							.awardTitle(raffleAwardEntity.getAwardTitle())
							.awardIndex(raffleAwardEntity.getSort())
							.build())
					.build();
		} catch (AppException e) {
			log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
			return Response.<ActivityDrawResponseDTO>builder()
					.code(e.getCode())
					.info(e.getInfo())
					.build();
		} catch (Exception e) {
			log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
			return Response.<ActivityDrawResponseDTO>builder()
					.code(ResponseCode.UN_ERROR.getCode())
					.info(ResponseCode.UN_ERROR.getInfo())
					.build();
		}
	}
}
