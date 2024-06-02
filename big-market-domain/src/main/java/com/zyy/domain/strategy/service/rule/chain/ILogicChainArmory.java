package com.zyy.domain.strategy.service.rule.chain;

/**
 * 责任链装配接口
 */
public interface ILogicChainArmory {
	ILogicChain next();

	ILogicChain appendNext(ILogicChain next);
}
