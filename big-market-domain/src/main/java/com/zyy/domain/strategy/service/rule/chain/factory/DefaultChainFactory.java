package com.zyy.domain.strategy.service.rule.chain.factory;

import com.zyy.domain.strategy.model.entity.StrategyEntity;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import com.zyy.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultChainFactory {

	private final Map<String, ILogicChain> logicChainGroup;
	protected IStrategyRepository repository;

	public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository repository) {
		this.logicChainGroup = logicChainGroup;
		this.repository = repository;
	}

	//通过strategyId，查询其ruleModels，构建责任链
	public ILogicChain openLogicChain(Long strategyId) {
		StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyById(strategyId);
		String[] ruleModels = strategyEntity.ruleModels();

		//如果未配置策略规则，则责任链中只含有一个默认责任节点
		if (null == ruleModels || 0 == ruleModels.length) return logicChainGroup.get("default");

		//按配置顺序装填责任链
		ILogicChain logicChain = logicChainGroup.get(ruleModels[0]);
		ILogicChain currentLoginNode = logicChain;
		for (int i = 1; i < ruleModels.length; i++) {
			currentLoginNode.appendNext(logicChainGroup.get(ruleModels[i]));
			currentLoginNode = currentLoginNode.next();
		}
		//最后装填默认责任节点
		currentLoginNode.appendNext(logicChainGroup.get("default"));
		return logicChain;
	}
}
