package com.zyy.test.infrastructure;

import com.zyy.domain.strategy.model.valobj.RuleTreeVO;
import com.zyy.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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
}
