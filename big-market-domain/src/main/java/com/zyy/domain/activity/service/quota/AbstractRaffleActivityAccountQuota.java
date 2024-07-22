package com.zyy.domain.activity.service.quota;

import com.zyy.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.zyy.domain.activity.model.entity.*;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.zyy.domain.activity.service.quota.rule.IActionChain;
import com.zyy.domain.activity.service.quota.rule.factory.DefaultActionChainFactory;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractRaffleActivityAccountQuota extends RaffleActivityAccountQuotaSupport implements IRaffleActivityAccountQuotaService {

	public AbstractRaffleActivityAccountQuota(IActivityRepository activityRepository, DefaultActionChainFactory defaultActionChainFactory) {
		super(defaultActionChainFactory, activityRepository);
	}

	@Override
	public String createOrder(SkuRechargeEntity skuRechargeEntity) {
		//1.参数校验
		String userId = skuRechargeEntity.getUserId();
		Long sku = skuRechargeEntity.getSku();
		String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
		if (null == sku || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
			throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
		}

		//2.查询基础信息
		ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
		ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
		ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

		//3.活动动作规则校验
		IActionChain actionChain = defaultActivityFactoryChain.openActionChain();
		actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);

		//4.构建订单聚合对象
		CreateQuotaOrderAggregate createQuotaOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);

		//5.保存订单
		doSaveOrder(createQuotaOrderAggregate);

		//6.返回单号
		return createQuotaOrderAggregate.getActivityOrderEntity().getOrderId();
	}

	protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity,
																	 ActivitySkuEntity activitySkuEntity,
																	 ActivityEntity activityEntity,
																	 ActivityCountEntity activityCountEntity);

	protected abstract void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
