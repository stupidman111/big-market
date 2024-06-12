package com.zyy.domain.strategy.service.rule.chain;


import com.zyy.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * 责任链接口
 */
public interface ILogicChain extends ILogicChainArmory {

	DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);
}
