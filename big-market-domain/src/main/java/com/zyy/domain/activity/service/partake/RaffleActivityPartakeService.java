package com.zyy.domain.activity.service.partake;

import com.zyy.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.zyy.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.zyy.domain.activity.model.entity.*;
import com.zyy.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.zyy.domain.activity.model.valobj.OrderStateVO;
import com.zyy.domain.activity.model.valobj.UserRaffleOrderStateVO;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.domain.activity.service.IRaffleActivitySkuStockService;
import com.zyy.domain.activity.service.quota.rule.factory.DefaultActionChainFactory;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RaffleActivityPartakeService extends AbstractRaffleActivityPartake {

	private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
	private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

	public RaffleActivityPartakeService(IActivityRepository activityRepository) {
		super(activityRepository);
	}

	@Override
	protected CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate) {
		//查询用户账户总额度
		ActivityAccountEntity activityAccountEntity = activityRepository.queryActivityAccountByUserId(userId, activityId);

		//额度判断
		if (null == activityAccountEntity || activityAccountEntity.getTotalCountSurplus() <= 0) {
			throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
		}

		String month = dateFormatMonth.format(currentDate);
		String day = dateFormatDay.format(currentDate);

		//查询月账户额度
		ActivityAccountMonthEntity activityAccountMonthEntity = activityRepository.queryActivityAccountMonthByUserId(userId, activityId, month);
		if (null != activityAccountMonthEntity && activityAccountMonthEntity.getMonthCountSurplus() <= 0) {
			throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getInfo());
		}

		boolean isExistAccountMonth = null != activityAccountMonthEntity;
		if (null == activityAccountMonthEntity) {
			activityAccountMonthEntity = ActivityAccountMonthEntity.builder()
					.userId(userId)
					.activityId(activityId)
					.month(month)
					.monthCount(activityAccountEntity.getMonthCount())
					.monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
					.build();
		}

		//查询日账户额度
		ActivityAccountDayEntity activityAccountDayEntity = activityRepository.queryActivityAccountDayByUserId(userId, activityId, day);
		if (null != activityAccountDayEntity && activityAccountDayEntity.getDayCountSurplus() <= 0) {
			throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getInfo());
		}

		boolean isExistAccountDay = null != activityAccountDayEntity;
		if (null == activityAccountDayEntity) {
			activityAccountDayEntity = ActivityAccountDayEntity.builder()
					.userId(userId)
					.activityId(activityId)
					.day(day)
					.dayCount(activityAccountEntity.getDayCount())
					.dayCountSurplus(activityAccountEntity.getDayCountSurplus())
					.build();
		}

		//构建对象
		CreatePartakeOrderAggregate createPartakeOrderAggregate = CreatePartakeOrderAggregate.builder()
				.userId(userId)
				.activityId(activityId)
				.activityAccountEntity(activityAccountEntity)
				.isExistAccountMonth(isExistAccountMonth)
				.activityAccountMonthEntity(activityAccountMonthEntity)
				.isExistAccountDay(isExistAccountDay)
				.activityAccountDayEntity(activityAccountDayEntity)
				.build();

		return createPartakeOrderAggregate;
	}

	@Override
	protected UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate) {
		//构建订单
		ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);

		UserRaffleOrderEntity userRaffleOrderEntity = UserRaffleOrderEntity.builder()
				.userId(userId)
				.activityId(activityId)
				.activityName(activityEntity.getActivityName())
				.strategyId(activityEntity.getStrategyId())
				.orderId(RandomStringUtils.randomNumeric(12))
				.orderTime(currentDate)
				.orderState(UserRaffleOrderStateVO.create)
				.build();

		return userRaffleOrderEntity;
	}
}
