package com.zyy.infrastructure.persistent.repository;

import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;
import com.zyy.domain.activity.model.valobj.ActivityStateVO;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.infrastructure.persistent.dao.IRaffleActivityCountDao;
import com.zyy.infrastructure.persistent.dao.IRaffleActivityDao;
import com.zyy.infrastructure.persistent.dao.IRaffleActivitySkuDao;
import com.zyy.infrastructure.persistent.po.RaffleActivity;
import com.zyy.infrastructure.persistent.po.RaffleActivityCount;
import com.zyy.infrastructure.persistent.po.RaffleActivitySku;
import com.zyy.infrastructure.persistent.redis.IRedisService;
import com.zyy.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
}
