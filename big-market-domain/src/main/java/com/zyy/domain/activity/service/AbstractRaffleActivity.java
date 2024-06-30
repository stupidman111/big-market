package com.zyy.domain.activity.service;

import com.zyy.domain.activity.model.aggregate.CreateOrderAggregate;
import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;
import com.zyy.domain.activity.model.entity.SkuRechargeEntity;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.domain.activity.service.rule.IActionChain;
import com.zyy.domain.activity.service.rule.factory.DefaultActionChainFactory;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleActivity extends RaffleActivitySupport implements IRaffleOrder {


	public AbstractRaffleActivity(DefaultActionChainFactory defaultActivityFactoryChain, IActivityRepository activityRepository) {
		super(defaultActivityFactoryChain, activityRepository);
	}

	@Override
	public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
		//1.参数校验
		String userId = skuRechargeEntity.getUserId();
		Long sku = skuRechargeEntity.getSku();
		String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
		if (null == sku || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
			throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
		}

		//2.获取信息
		ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
		ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
		ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

		//3.活动动作规则校验
		IActionChain actionChain = defaultActivityFactoryChain.openActionChain();
		boolean success = actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);

		//4.构建订单聚合对象
		CreateOrderAggregate createOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);

		//5.保存订单（）
		doSaveOrder(createOrderAggregate);

		//6.返回单号
		return createOrderAggregate.getActivityOrderEntity().getOrderId();
	}

	protected abstract CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity,
																ActivitySkuEntity activitySkuEntity,
																ActivityEntity activityEntity,
																ActivityCountEntity activityCountEntity);

	protected abstract void doSaveOrder(CreateOrderAggregate createOrderAggregate);

}
