package com.zyy.domain.activity.service.rule.impl;

import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;
import com.zyy.domain.activity.model.valobj.ActivityStateVO;
import com.zyy.domain.activity.service.rule.AbstractActionChain;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import com.zyy.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {
	@Override
	public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
		log.info("活动责任链-基础信息【有效期、状态、库存（sku）】校验开始, " +
						"sku:{}, activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());

		/**
		 * 校验：活动是否为开启状态
		 */
		if (!ActivityStateVO.open.equals(activityEntity.getState())) {
			throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
		}

		/**
		 * 校验：
		 * 	活动开始时间在当前时间之后--活动未开启
		 * 	活动结束时间在当前时间之前--活动已结束
		 */
		Date currentDate = new Date();
		if (activityEntity.getBeginDateTime().after(currentDate) ||
			activityEntity.getEndDateTime().before(currentDate)) {
			throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
		}

		/**
		 * 校验：
		 * 	活动sku库存
		 */
		if (activitySkuEntity.getStockCountSurplus() <= 0) {
			throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
		}


		return next().action(activitySkuEntity, activityEntity, activityCountEntity);
	}
}
