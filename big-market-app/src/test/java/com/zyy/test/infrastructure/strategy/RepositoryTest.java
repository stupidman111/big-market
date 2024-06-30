package com.zyy.test.infrastructure.strategy;

import com.alibaba.fastjson.JSON;
import com.zyy.domain.strategy.model.entity.StrategyAwardEntity;
import com.zyy.domain.strategy.model.valobj.RuleTreeVO;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest {

	@Resource
	private IStrategyRepository repository;

	@Test
	public void test_queryRuleTreeVOByTreeId() {
		RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId("tree_lock");

		ruleTreeVO.getTreeNodeMap().forEach((nodeName, node) -> {
			System.out.println(nodeName + "------" + node.getTreeNodeLineVOList());
		});

		//log.info("查询结果：{}", ruleTreeVO);
	}

	//void updateStrategyAwardStock(Long strategyId, Integer awardId);
	@Test
	public void test_updateStrategyAwardStock() {
		repository.updateStrategyAwardStock(100001L, 101);
	}


	@Test
	public void test_queryStrategyAwardList() {
		List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(100001L);
		for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
			log.info("ans: {}\n", JSON.toJSONString(strategyAwardEntity));
		}
	}
}
