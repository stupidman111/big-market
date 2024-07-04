package com.zyy.domain.activity.service.rule.impl;

import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;
import com.zyy.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.domain.activity.service.armory.IActivityArmory;
import com.zyy.domain.activity.service.armory.IActivityDispatch;
import com.zyy.domain.activity.service.rule.AbstractActionChain;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {

	@Resource
	private IActivityDispatch activityDispatch;

	@Resource
	private IActivityRepository activityRepository;

	@Override
	public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
		log.info("活动责任链-商品库存处理【有效期、状态、库存(sku)】开始, " +
				"sku:{}, activityId:{}",
				activitySkuEntity.getSku(), activityEntity.getActivityId());

		// 扣减库存
		boolean status = activityDispatch.subtractionActivitySkuStock(activitySkuEntity.getSku(), activityEntity.getEndDateTime());

		if (status) {
			log.info("活动责任链-商品库存处理【有效期、状态、库存(sku)】成功, " +
					"sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());

			//写入延迟队列，延迟消费更新库存记录（双重延缓：写到延迟队列 + 定时任务扫描）
			activityRepository.activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO.builder()
					.sku(activitySkuEntity.getSku())
					.activityId(activityEntity.getActivityId())
					.build());

			return true;
		}


		throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
	}
}
