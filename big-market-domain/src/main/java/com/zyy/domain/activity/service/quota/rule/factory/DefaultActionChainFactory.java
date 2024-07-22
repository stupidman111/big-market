package com.zyy.domain.activity.service.quota.rule.factory;

import com.zyy.domain.activity.service.quota.rule.IActionChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultActionChainFactory {

	private final IActionChain actionChain;

	/**
	 * 会自动将两个 IActionChain的 具体实现类：ActivityBaseActionChain、ActivitySkuStockActionChain装配进 map
	 * @param actionChainGroup
	 */
	public DefaultActionChainFactory(Map<String, IActionChain> actionChainGroup) {
		actionChain = actionChainGroup.get(ActionModel.activity_base_action.code);

		actionChain.appendNext(actionChainGroup.get(ActionModel.activity_sku_stock_action.getCode()));
	}

	public IActionChain openActionChain() {
		return this.actionChain;
	}

	@Getter
	@AllArgsConstructor
	public enum ActionModel {
		activity_base_action("activity_base_action", "活动的库存、时间校验"),
		activity_sku_stock_action("activity_sku_stock_action", "活动 sku 库存"),
		;

		private final String code;
		private final String info;
	}
}
