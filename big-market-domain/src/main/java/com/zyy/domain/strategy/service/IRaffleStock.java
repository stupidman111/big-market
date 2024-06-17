package com.zyy.domain.strategy.service;

import com.zyy.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * 抽奖库存相关服务
 */
public interface IRaffleStock {

	/**
	 * 获取奖品库存消耗队列
	 * @return
	 * @throws InterruptedException
	 */
	StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;


	/**
	 * 更新奖品库存消耗记录
	 */
	void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
