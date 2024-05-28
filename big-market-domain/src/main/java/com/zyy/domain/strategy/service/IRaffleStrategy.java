package com.zyy.domain.strategy.service;

import com.zyy.domain.strategy.model.entity.RaffleAwardEntity;
import com.zyy.domain.strategy.model.entity.RaffleFactorEntity;

public interface IRaffleStrategy {

	RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}

