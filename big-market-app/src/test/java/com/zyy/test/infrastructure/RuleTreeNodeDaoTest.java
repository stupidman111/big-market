package com.zyy.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.zyy.infrastructure.persistent.dao.IRuleTreeNodeDao;
import com.zyy.infrastructure.persistent.po.RuleTreeNode;
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
public class RuleTreeNodeDaoTest {

	@Resource
	private IRuleTreeNodeDao ruleTreeNodeDao;

	@Test
	public void test_queryRuleTreeNodeListByTreeId() {
		List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId("tree_lock");
		log.info("查询结果：{}", JSON.toJSONString(ruleTreeNodes));
	}
}
