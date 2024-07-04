package com.zyy.infrastructure.persistent.repository;

import com.zyy.domain.activity.event.ActivitySkuStockZeroMessageEvent;
import com.zyy.domain.activity.model.aggregate.CreateOrderAggregate;
import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivityOrderEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;
import com.zyy.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.zyy.domain.activity.model.valobj.ActivityStateVO;
import com.zyy.domain.activity.model.valobj.OrderStateVO;
import com.zyy.domain.activity.repository.IActivityRepository;
import com.zyy.infrastructure.event.EventPublisher;
import com.zyy.infrastructure.persistent.dao.*;
import com.zyy.infrastructure.persistent.po.*;
import com.zyy.infrastructure.persistent.redis.IRedisService;
import com.zyy.types.common.Constants;
import com.zyy.types.enums.ResponseCode;
import com.zyy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

	@Resource
	private EventPublisher eventPublisher;

	@Resource
	private ActivitySkuStockZeroMessageEvent activitySkuStockZeroMessageEvent;

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

	@Override
	public void cacheActivitySkuStockCount(String cacheKey, Integer stockCount) {
		//if (redisService.isExists(cacheKey)) return;
		redisService.setAtomicLong(cacheKey, stockCount);
	}

	@Override
	public boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime) {
		int surplus = (int) redisService.decr(cacheKey);//decr是原子性的，保证不会有并发问题

		//扣减完后，库存若等于 0，表示已经消耗光了，直接发送 MQ消息 通知清空库存
		if (surplus == 0) {
			//TODO 发送 MQ 消息更新数据库库存为 0
			eventPublisher.publish(activitySkuStockZeroMessageEvent.topic(),
					activitySkuStockZeroMessageEvent.buildEventMessage(sku));
			return false;
		}
		//扣减完后，若库存小于 0，表示超卖了，因此恢复到 0 个
		if (surplus < 0) {
			redisService.setAtomicLong(cacheKey, 0);//原子操作
			return false;
		}

		/**
		 * 对当前扣减前的剩余库存使用setNx加锁，保证在【人工恢复库存等操作下】不会超卖
		 * 因为已经扣减
		 */
		String lockKey = cacheKey + Constants.UNDERLINE + (surplus + 1);
		long expireMills = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
		Boolean lock = redisService.setNx(lockKey, expireMills, TimeUnit.MILLISECONDS);
		if (!lock) {
			log.info("活动sku 库存扣减失败 {}", lockKey);
		}
		return lock;
	}

	@Override
	public void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO) {
		String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY;
		RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
		RDelayedQueue<ActivitySkuStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
		delayedQueue.offer(activitySkuStockKeyVO, 3, TimeUnit.SECONDS);
	}

	@Override
	public ActivitySkuStockKeyVO takeQueueValue() {
		String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY;
		RBlockingQueue<ActivitySkuStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
		return destinationQueue.poll();
	}

	@Override
	public void clearQueueValue() {
		String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY;
		RBlockingQueue<ActivitySkuStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
		destinationQueue.clear();
	}

	@Override
	public void updateActivitySkuStock(Long sku) {//sku库存-1
		raffleActivitySkuDao.updateActivitySkuStock(sku);
	}

	@Override
	public void clearActivitySkuStock(Long sku) {//清空库存
		raffleActivitySkuDao.clearActivitySkuStock(sku);
	}
}
