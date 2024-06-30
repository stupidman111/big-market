package com.zyy.test.infrastructure.strategy;

import com.alibaba.fastjson.JSON;
import com.zyy.infrastructure.persistent.dao.IRuleTreeDao;
import com.zyy.infrastructure.persistent.po.RuleTree;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTreeDaoTest {

	@Resource
	private IRuleTreeDao ruleTreeDao;

	@Test
	public void test_queryRuleTreeByTreeId() {
		RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId("tree_lock");
		log.info("查询结果：{}", JSON.toJSONString(ruleTree));
	}
}
