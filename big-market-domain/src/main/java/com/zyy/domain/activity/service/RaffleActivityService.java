package com.zyy.domain.activity.service;

import com.zyy.domain.activity.model.aggregate.CreateOrderAggregate;
import com.zyy.domain.activity.model.entity.*;
import com.zyy.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.zyy.domain.activity.model.valobj.OrderStateVO;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.domain.activity.service.rule.factory.DefaultActionChainFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RaffleActivityService extends AbstractRaffleActivity implements ISkuStock {
	public RaffleActivityService(IActivityRepository activityRepository, DefaultActionChainFactory defaultActivityFactoryChain) {
		super(defaultActivityFactoryChain, activityRepository);
	}

	@Override
	protected CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
		//创建订单实体对象
		ActivityOrderEntity activityOrderEntity = ActivityOrderEntity.builder()
				.userId(skuRechargeEntity.getUserId())
				.sku(skuRechargeEntity.getSku())
				.activityId(activitySkuEntity.getActivityId())
				.activityName(activityEntity.getActivityName())
				.strategyId(activityEntity.getStrategyId())
				.orderId(RandomStringUtils.randomNumeric(12))//一般用雪花算法，这里简单使用 12位 id
				.orderTime(new Date())
				.totalCount(activityCountEntity.getTotalCount())
				.monthCount(activityCountEntity.getMonthCount())
				.dayCount(activityCountEntity.getDayCount())
				.state(OrderStateVO.completed)
				.outBusinessNO(skuRechargeEntity.getOutBusinessNo())
				.build();

		return CreateOrderAggregate.builder()
				.userId(skuRechargeEntity.getUserId())
				.activityId(activityEntity.getActivityId())
				.totalCount(activityCountEntity.getTotalCount())
				.monthCount(activityCountEntity.getMonthCount())
				.dayCount(activityCountEntity.getDayCount())
				.activityOrderEntity(activityOrderEntity)
				.build();
	}

	@Override
	protected void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
		activityRepository.doSaveOrder(createOrderAggregate);
	}

	@Override
	public ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException {
		return activityRepository.takeQueueValue();
	}

	@Override
	public void clearQueueValue() {
		activityRepository.clearQueueValue();
	}

	@Override
	public void updateActivitySkuStock(Long sku) {
		activityRepository.updateActivitySkuStock(sku);
	}

	@Override
	public void clearActivitySkuStock(Long sku) {
		activityRepository.clearActivitySkuStock(sku);
	}
}
