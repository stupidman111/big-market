package com.zyy.domain.activity.service.rule;

import com.zyy.domain.activity.model.entity.ActivityCountEntity;
import com.zyy.domain.activity.model.entity.ActivityEntity;
import com.zyy.domain.activity.model.entity.ActivitySkuEntity;

public abstract class AbstractActionChain implements IActionChain {

	private IActionChain next;

	@Override
	public IActionChain next() {
		return next;
	}

	@Override
	public IActionChain appendNext(IActionChain next) {
		this.next = next;
		return next;
	}
}
