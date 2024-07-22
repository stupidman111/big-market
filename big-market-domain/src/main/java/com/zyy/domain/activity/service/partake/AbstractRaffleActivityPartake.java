package com.zyy.domain.activity.service.partake;

import com.alibaba.fastjson.JSON;
import com.zyy.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.zyy.domain.activity.model.entity.*;
import com.zyy.domain.activity.model.valobj.ActivityStateVO;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.zyy.domain.activity.service.IRaffleActivityPartakeService;
import com.zyy.domain.activity.service.quota.RaffleActivityAccountQuotaSupport;
import com.zyy.domain.activity.service.quota.rule.IActionChain;
import com.zyy.domain.activity.service.quota.rule.factory.DefaultActionChainFactory;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Slf4j
public abstract class AbstractRaffleActivityPartake implements IRaffleActivityPartakeService {

	protected final IActivityRepository activityRepository;

	public AbstractRaffleActivityPartake(IActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}


	@Override
	public UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
		//0.基础信息
		String userId = partakeRaffleActivityEntity.getUserId();
		Long activityId = partakeRaffleActivityEntity.getActivityId();
		Date currentDate = new Date();

		//1.活动查询&活动状态校验&活动日期校验
		ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);
		if (!ActivityStateVO.open.equals(activityEntity.getState())) {
			throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
		}
		if (activityEntity.getBeginDateTime().after(currentDate) || activityEntity.getEndDateTime().before(currentDate)) {
			throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
		}

		//2.查询未被使用的活动参与订单
		UserRaffleOrderEntity userRaffleOrderEntity = activityRepository.queryNoUsedRaffleOrder(partakeRaffleActivityEntity);
		if (null != userRaffleOrderEntity) {
			log.info("创建参与活动订单 userId:{} activityId:{} userRaffleOrderEntity:{}", userId, activityId, JSON.toJSONString(userRaffleOrderEntity));
			return userRaffleOrderEntity;
		}

		//3.额度账户过滤&返回账户构建对象
		CreatePartakeOrderAggregate createPartakeOrderAggregate = this.doFilterAccount(userId, activityId, currentDate);

		//4.构建订单
		UserRaffleOrderEntity userRaffleOrder = this.buildUserRaffleOrder(userId, activityId, currentDate);

		//5.填充抽奖单实体对象
		createPartakeOrderAggregate.setUserRaffleOrderEntity(userRaffleOrder);

		//6.保存聚合对象
		activityRepository.saveCreatePartakeOrderAggregate(createPartakeOrderAggregate);

		//7.返回订单信息
		return userRaffleOrder;
	}

	protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate);

	protected abstract UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate);


}
