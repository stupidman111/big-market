package com.zyy.domain.strategy.service.armory;

import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Service
public class StrategyArmory implements IStrategyArmory {

	@Resource
	private IStrategyRepository repository;

	@Override
	public boolean assembleLotteryStrategy(Long strategyId) {
		//1.根据策略ID，获取【策略-奖品】实体-列表
		List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

		//2.在【策略-奖品】实体-列表中获取最小的概率
		BigDecimal minAwardRate = strategyAwardEntities.stream()
				.map(StrategyAwardEntity::getAwardRate)
				.min(BigDecimal::compareTo)
				.orElse(BigDecimal.ZERO);

		//3.计算【策略-奖品】实体-列表 的总概率
		BigDecimal totalAwardRate = strategyAwardEntities.stream()
				.map(StrategyAwardEntity::getAwardRate)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		//4. 总概率 / 最小概率，将概率以最小概率为分段范围，比如：三个实体对应概率：0.2，0.3，0.5，总概率为1，1 / 0.2 = 5，那么基准就有5个分段
		BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

		//5. 将所有实体的概率，按照 概率 * 分段范围 获取实际有n个实体的id存到list中；
		List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
		for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
			Integer awardId = strategyAward.getAwardId();
			BigDecimal awardRate = strategyAward.getAwardRate();
			for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
				strategyAwardSearchRateTables.add(awardId);
			}
		}

		//6. 乱序操作
		Collections.shuffle(strategyAwardSearchRateTables);

		//7. 生成map集合，
		Map<Integer, Integer> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
		for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
			shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
		}

		repository.storeStrategyAwardSearchRateTable(strategyId, shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);
		return true;
	}

	@Override
	public Integer getRandomAwardId(Long strategyId) {
		int rateRange = repository.getRateRange(strategyId);
		return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
	}
}
