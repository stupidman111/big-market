package com.zyy.infrastructure.persistent.repository;

import com.zyy.domain.activity.model.aggregate.CreateOrderAggregate;
import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivityOrderEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;
import com.zyy.domain.activity.model.valobj.ActivityStateVO;
import com.zyy.domain.activity.model.valobj.OrderStateVO;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.infrastructure.persistent.dao.*;
import com.zyy.infrastructure.persistent.po.*;
import com.zyy.infrastructure.persistent.redis.IRedisService;
import com.zyy.types.common.Constants;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Repository
public class ActivityRepository implements IActivityRepository {

	@Resource
	private IRedisService redisService;

	@Resource
	private IRaffleActivityDao raffleActivityDao;

	@Resource
	private IRaffleActivitySkuDao raffleActivitySkuDao;

	@Resource
	private IRaffleActivityCountDao raffleActivityCountDao;

	@Resource
	private IRaffleActivityOrderDao raffleActivityOrderDao;

	@Resource
	private IRaffleActivityAccountDao raffleActivityAccountDao;

	@Override
	public ActivitySkuEntity queryActivitySku(Long sku) {
		//1.查缓存
		String cacheKey = Constants.RedisKey.ACTIVITY_SKU_KEY + sku;
		ActivitySkuEntity activitySkuEntity = redisService.getValue(cacheKey);
		if (null != activitySkuEntity) return activitySkuEntity;

		//2.缓存未命中，走库
		RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(sku);
		activitySkuEntity = ActivitySkuEntity.builder()
				.sku(sku)
				.activityId(raffleActivitySku.getActivityId())
				.activityCountId(raffleActivitySku.getActivityCountId())
				.stockCount(raffleActivitySku.getStockCount())
				.stockCountSurplus(raffleActivitySku.getStockCountSurplus())
				.build();

		//3.写缓存
		redisService.setValue(cacheKey, activitySkuEntity);

		//4.返回
		return activitySkuEntity;
	}

	@Override
	public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
		//1.查缓存
		String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
		ActivityEntity activityEntity = redisService.getValue(cacheKey);
		if (null != activityEntity) return activityEntity;

		//2.缓存未命中，走库
		RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(activityId);
		activityEntity = ActivityEntity.builder()
				.activityId(activityId)
				.activityName(raffleActivity.getActivityName())
				.activityDesc(raffleActivity.getActivityDesc())
				.beginDateTime(raffleActivity.getBeginDateTime())
				.endDateTime(raffleActivity.getEndDateTime())
				.strategyId(raffleActivity.getStrategyId())
				.state(ActivityStateVO.valueOf(raffleActivity.getState()))
				.build();

		//3.写缓存
		redisService.setValue(cacheKey, activityEntity);

		//4.返回
		return activityEntity;
	}

	@Override
	public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
		//1.查缓存
		String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
		ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
		if (null != activityCountEntity) return activityCountEntity;

		//2.缓存未命中，走库
		RaffleActivityCount raffleActivityCount = raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);
		activityCountEntity = ActivityCountEntity.builder()
				.activityCountId(activityCountId)
				.totalCount(raffleActivityCount.getTotalCount())
				.monthCount(raffleActivityCount.getMonthCount())
				.dayCount(raffleActivityCount.getDayCount())
				.build();

		//3.写缓存
		redisService.setValue(cacheKey, activityCountEntity);

		//4.返回
		return activityCountEntity;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	@Override
	public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
		//获取抽奖订单实体
		ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();

		//通过 抽奖订单实体 构建 抽奖订单PO
		RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
		raffleActivityOrder.setUserId(activityOrderEntity.getUserId());
		raffleActivityOrder.setSku(activityOrderEntity.getSku());
		raffleActivityOrder.setActivityId(activityOrderEntity.getActivityId());
		raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
		raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
		raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
		raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
		raffleActivityOrder.setTotalCount(activityOrderEntity.getTotalCount());
		raffleActivityOrder.setMonthCount(activityOrderEntity.getMonthCount());
		raffleActivityOrder.setDayCount(activityOrderEntity.getDayCount());
		raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
		raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNO());

		RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
		raffleActivityAccount.setUserId(createOrderAggregate.getUserId());
		raffleActivityAccount.setActivityId(createOrderAggregate.getActivityId());
		raffleActivityAccount.setTotalCount(createOrderAggregate.getTotalCount());
		raffleActivityAccount.setTotalCountSurplus(createOrderAggregate.getTotalCount());
		raffleActivityAccount.setDayCount(createOrderAggregate.getDayCount());
		raffleActivityAccount.setDayCountSurplus(createOrderAggregate.getDayCount());
		raffleActivityAccount.setMonthCount(createOrderAggregate.getMonthCount());
		raffleActivityAccount.setMonthCountSurplus(createOrderAggregate.getMonthCount());

		try {
			raffleActivityOrderDao.insert(raffleActivityOrder);
			//更行账户次数配额
			int count = raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
			//若返回 0，表示没有任何行收到影响，则账户不存在，创建新账户
			if (0 == count) {
				raffleActivityAccountDao.insert(raffleActivityAccount);
			}

			//int i = 10 / 0;//测试事务是否生效
		} catch (Exception e) {
			log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(), activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
			throw new AppException(ResponseCode.INDEX_DUP.getCode());
		}
	}
}
