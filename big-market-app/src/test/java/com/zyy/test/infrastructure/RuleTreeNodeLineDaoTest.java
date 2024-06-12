package com.zyy.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.zyy.infrastructure.persistent.dao.IRuleTreeNodeLineDao;
import com.zyy.infrastructure.persistent.po.RuleTreeNodeLine;
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
public class RuleTreeNodeLineDaoTest {

	@Resource
	private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

	@Test
	public void test_queryRuleTreeNodeLineListByTreeId() {
		List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId("tree_lock");
		log.info("查询结果：{}", JSON.toJSONString(ruleTreeNodeLines));
	}
}
