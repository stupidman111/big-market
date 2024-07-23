package com.zyy.domain.activity.service.armory;

import com.zyy.domain.activity.model.entity.ActivitySkuEntity;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class IActivityArmoryDispatch implements IActivityArmory, IActivityDispatch {

	@Resource
	private IActivityRepository activityRepository;

	@Override
	public boolean assemblyActivitySku(Long sku) {
		log.info("活动 sku 库存装配开始：sku:{}", sku);
		ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);

		//预热活动sku库存
		cacheActivitySkuStockCount(sku, activitySkuEntity.getStockCount());

		//预热活动
		activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());

		//预热活动次数
		activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

		return true;
	}

	@Override
	public boolean assembleActivitySkuByActivityId(Long activityId) {

		List<ActivitySkuEntity> activitySkuEntities = activityRepository.queryActivitySkuListByActivityId(activityId);
		for (ActivitySkuEntity activitySkuEntity : activitySkuEntities) {
			cacheActivitySkuStockCount(activitySkuEntity.getSku(), activitySkuEntity.getStockCountSurplus());
			//预热活动次数
			activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
		}
		//预热活动
		activityRepository.queryRaffleActivityByActivityId(activityId);
		return true;
	}

	private void cacheActivitySkuStockCount(Long sku, Integer stockCount) {
		String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
		activityRepository.cacheActivitySkuStockCount(cacheKey, stockCount);
	}

	@Override
	public boolean subtractionActivitySkuStock(Long sku, Date endDateTime) {
		String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
		return activityRepository.subtractionActivitySkuStock(sku, cacheKey, endDateTime);
	}
}
