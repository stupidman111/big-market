package com.zyy.infrastructure.persistent.repository;

import com.zyy.domain.activity.event.ActivitySkuStockZeroMessageEvent;
import com.zyy.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.zyy.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.zyy.domain.activity.model.entity.*;
import com.zyy.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.zyy.domain.activity.model.valobj.ActivityStateVO;
import com.zyy.domain.activity.model.valobj.UserRaffleOrderStateVO;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

	@Resource
	private IUserRaffleOrderDao userRaffleOrderDao;

	@Resource
	private IRaffleActivityAccountMonthDao raffleActivityAccountMonthDao;

	@Resource
	private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;
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
	public void doSaveOrder(CreateQuotaOrderAggregate createPartakeOrderAggregate) {
		//获取抽奖订单实体
		ActivityOrderEntity activityOrderEntity = createPartakeOrderAggregate.getActivityOrderEntity();

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
		raffleActivityAccount.setUserId(createPartakeOrderAggregate.getUserId());
		raffleActivityAccount.setActivityId(createPartakeOrderAggregate.getActivityId());
		raffleActivityAccount.setTotalCount(createPartakeOrderAggregate.getTotalCount());
		raffleActivityAccount.setTotalCountSurplus(createPartakeOrderAggregate.getTotalCount());
		raffleActivityAccount.setDayCount(createPartakeOrderAggregate.getDayCount());
		raffleActivityAccount.setDayCountSurplus(createPartakeOrderAggregate.getDayCount());
		raffleActivityAccount.setMonthCount(createPartakeOrderAggregate.getMonthCount());
		raffleActivityAccount.setMonthCountSurplus(createPartakeOrderAggregate.getMonthCount());

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

	@Override
	public UserRaffleOrderEntity queryNoUsedRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
		UserRaffleOrder userRaffleOrderReq = new UserRaffleOrder();
		userRaffleOrderReq.setActivityId(partakeRaffleActivityEntity.getActivityId());
		userRaffleOrderReq.setUserId(partakeRaffleActivityEntity.getUserId());

		UserRaffleOrder userRaffleOrderRes = userRaffleOrderDao.queryNoUsedRaffleOrder(userRaffleOrderReq);
		if (null == userRaffleOrderRes) return null;//没有未被使用的订单

		//封装信息
		UserRaffleOrderEntity userRaffleOrderEntity = UserRaffleOrderEntity.builder()
				.userId(userRaffleOrderRes.getUserId())
				.activityId(userRaffleOrderRes.getActivityId())
				.activityName(userRaffleOrderRes.getActivityName())
				.strategyId(userRaffleOrderRes.getStrategyId())
				.orderId(userRaffleOrderRes.getOrderId())
				.orderTime(userRaffleOrderRes.getOrderTime())
				.orderState(UserRaffleOrderStateVO.valueOf(userRaffleOrderRes.getOrderState()))
				.build();

		return userRaffleOrderEntity;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate) {
		String userId = createPartakeOrderAggregate.getUserId();
		Long activityId = createPartakeOrderAggregate.getActivityId();
		ActivityAccountEntity activityAccountEntity = createPartakeOrderAggregate.getActivityAccountEntity();
		ActivityAccountMonthEntity activityAccountMonthEntity = createPartakeOrderAggregate.getActivityAccountMonthEntity();
		ActivityAccountDayEntity activityAccountDayEntity = createPartakeOrderAggregate.getActivityAccountDayEntity();
		UserRaffleOrderEntity userRaffleOrderEntity = createPartakeOrderAggregate.getUserRaffleOrderEntity();

		try {
			// 1. 更新总账户
			int totalCount = raffleActivityAccountDao.updateActivityAccountSubtractionQuota(
					RaffleActivityAccount.builder()
							.userId(userId)
							.activityId(activityId)
							.build());
			if (1 != totalCount) {
				log.warn("写入创建参与活动记录，更新总账户额度不足，异常 userId: {} activityId: {}", userId, activityId);
				throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
			}

			// 2. 创建或更新月账户，true - 存在则更新，false - 不存在则插入
			if (createPartakeOrderAggregate.isExistAccountMonth()) {
				int updateMonthCount = raffleActivityAccountMonthDao.updateActivityAccountMonthSubtractionQuota(
						RaffleActivityAccountMonth.builder()
								.userId(userId)
								.activityId(activityId)
								.month(activityAccountMonthEntity.getMonth())
								.build());
				if (1 != updateMonthCount) {
					log.warn("写入创建参与活动记录，更新月账户额度不足，异常 userId: {} activityId: {} month: {}", userId, activityId, activityAccountMonthEntity.getMonth());
					throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getInfo());
				}
			} else {
				raffleActivityAccountMonthDao.insertActivityAccountMonth(RaffleActivityAccountMonth.builder()
						.userId(activityAccountMonthEntity.getUserId())
						.activityId(activityAccountMonthEntity.getActivityId())
						.month(activityAccountMonthEntity.getMonth())
						.monthCount(activityAccountMonthEntity.getMonthCount())
						.monthCountSurplus(activityAccountMonthEntity.getMonthCountSurplus() - 1)
						.build());
				raffleActivityAccountDao.updateActivityAccountMonthSurplusImageQuota(RaffleActivityAccount.builder()
						.userId(userId)
						.activityId(activityId)
						.monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
						.build());
			}

			// 3. 创建或更新日账户，true - 存在则更新，false - 不存在则插入
			if (createPartakeOrderAggregate.isExistAccountDay()) {
				int updateDayCount = raffleActivityAccountDayDao.updateActivityAccountDaySubtractionQuota(RaffleActivityAccountDay.builder()
						.userId(userId)
						.activityId(activityId)
						.day(activityAccountDayEntity.getDay())
						.build());
				if (1 != updateDayCount) {
					log.warn("写入创建参与活动记录，更新日账户额度不足，异常 userId: {} activityId: {} day: {}", userId, activityId, activityAccountDayEntity.getDay());
					throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getInfo());
				}
			} else {
				raffleActivityAccountDayDao.insertActivityAccountDay(RaffleActivityAccountDay.builder()
						.userId(activityAccountDayEntity.getUserId())
						.activityId(activityAccountDayEntity.getActivityId())
						.day(activityAccountDayEntity.getDay())
						.dayCount(activityAccountDayEntity.getDayCount())
						.dayCountSurplus(activityAccountDayEntity.getDayCountSurplus() - 1)
						.build());
				raffleActivityAccountDao.updateActivityAccountDaySurplusImageQuota(RaffleActivityAccount.builder()
						.userId(userId)
						.activityId(activityId)
						.dayCountSurplus(activityAccountEntity.getDayCountSurplus())
						.build());
			}

			// 4. 写入参与活动订单
			userRaffleOrderDao.insert(UserRaffleOrder.builder()
					.userId(userRaffleOrderEntity.getUserId())
					.activityId(userRaffleOrderEntity.getActivityId())
					.activityName(userRaffleOrderEntity.getActivityName())
					.strategyId(userRaffleOrderEntity.getStrategyId())
					.orderId(userRaffleOrderEntity.getOrderId())
					.orderTime(userRaffleOrderEntity.getOrderTime())
					.orderState(userRaffleOrderEntity.getOrderState().getCode())
					.build());
		} catch (DuplicateKeyException e) {
			log.error("写入创建参与活动记录，唯一索引冲突 userId: {} activityId: {}", userId, activityId, e);
			throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
		}
	}

	@Override
	public ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId) {
		RaffleActivityAccount raffleActivityAccountReq = new RaffleActivityAccount();
		raffleActivityAccountReq.setUserId(userId);
		raffleActivityAccountReq.setActivityId(activityId);

		RaffleActivityAccount raffleActivityAccountRes = raffleActivityAccountDao.queryActivityAccountByUserId(raffleActivityAccountReq);
		if (null == raffleActivityAccountRes) return null;

		return ActivityAccountEntity.builder()
				.userId(raffleActivityAccountRes.getUserId())
				.activityId(raffleActivityAccountRes.getActivityId())
				.totalCount(raffleActivityAccountRes.getTotalCount())
				.totalCountSurplus(raffleActivityAccountRes.getTotalCountSurplus())
				.dayCount(raffleActivityAccountRes.getDayCount())
				.dayCountSurplus(raffleActivityAccountRes.getDayCountSurplus())
				.monthCount(raffleActivityAccountRes.getMonthCount())
				.monthCountSurplus(raffleActivityAccountRes.getMonthCountSurplus())
				.build();
	}

	@Override
	public ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month) {
		RaffleActivityAccountMonth raffleActivityAccountMonthReq = new RaffleActivityAccountMonth();
		raffleActivityAccountMonthReq.setUserId(userId);
		raffleActivityAccountMonthReq.setActivityId(activityId);
		raffleActivityAccountMonthReq.setMonth(month);

		RaffleActivityAccountMonth raffleActivityAccountMonthRes = raffleActivityAccountMonthDao.queryActivityAccountMonthByUserId(raffleActivityAccountMonthReq);
		if (null == raffleActivityAccountMonthRes) return null;

		return ActivityAccountMonthEntity.builder()
				.userId(raffleActivityAccountMonthRes.getUserId())
				.activityId(raffleActivityAccountMonthRes.getActivityId())
				.month(raffleActivityAccountMonthRes.getMonth())
				.monthCount(raffleActivityAccountMonthRes.getMonthCount())
				.monthCountSurplus(raffleActivityAccountMonthRes.getMonthCountSurplus())
				.build();
	}

	@Override
	public ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day) {
		RaffleActivityAccountDay raffleActivityAccountDayReq = new RaffleActivityAccountDay();
		raffleActivityAccountDayReq.setUserId(userId);
		raffleActivityAccountDayReq.setActivityId(activityId);
		raffleActivityAccountDayReq.setDay(day);

		RaffleActivityAccountDay raffleActivityAccountDayRes = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(raffleActivityAccountDayReq);
		if (null == raffleActivityAccountDayRes) return null;

		return ActivityAccountDayEntity.builder()
				.userId(raffleActivityAccountDayRes.getUserId())
				.activityId(raffleActivityAccountDayRes.getActivityId())
				.day(raffleActivityAccountDayRes.getDay())
				.dayCount(raffleActivityAccountDayRes.getDayCount())
				.dayCountSurplus(raffleActivityAccountDayRes.getDayCountSurplus())
				.build();
	}

	@Override
	public List<ActivitySkuEntity> queryActivitySkuListByActivityId(Long activityId) {
		List<RaffleActivitySku> raffleActivitySkuList = raffleActivitySkuDao.queryActivitySkuListByActivityId(activityId);
		List<ActivitySkuEntity> activitySkuEntities = new ArrayList<>();

		for (RaffleActivitySku raffleActivitySku : raffleActivitySkuList) {
			activitySkuEntities.add(ActivitySkuEntity.builder()
					.sku(raffleActivitySku.getSku())
					.activityId(activityId)
					.activityCountId(raffleActivitySku.getActivityCountId())
					.stockCount(raffleActivitySku.getStockCount())
					.stockCountSurplus(raffleActivitySku.getStockCountSurplus())
					.build());
		}

		return activitySkuEntities;
	}

	@Override
	public Integer queryRaffleActivityDayPartakeCount(Long activityId, String userId) {
		RaffleActivityAccountDay raffleActivityAccountDay = new RaffleActivityAccountDay();
		raffleActivityAccountDay.setActivityId(activityId);
		raffleActivityAccountDay.setUserId(userId);
		raffleActivityAccountDay.setDay(raffleActivityAccountDay.currentDay());

		Integer dayPartakeCount = raffleActivityAccountDayDao.queryRaffleActivityAccountDayPartakeCount(raffleActivityAccountDay);
		return null == dayPartakeCount ? 0 : dayPartakeCount;
	}
}
