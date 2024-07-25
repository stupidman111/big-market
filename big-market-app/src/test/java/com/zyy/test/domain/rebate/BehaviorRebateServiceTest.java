package com.zyy.test.domain.rebate;

import com.alibaba.fastjson2.JSON;
import com.zyy.domain.rebate.model.entity.BehaviorEntity;
import com.zyy.domain.rebate.model.valobj.BehaviorTypeVO;
import com.zyy.domain.rebate.service.IBehaviorRebateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BehaviorRebateServiceTest {

	@Resource
	private IBehaviorRebateService behaviorRebateService;

	@Test
	public void test_createOrder() {
		BehaviorEntity behaviorEntity = new BehaviorEntity();

		behaviorEntity.setUserId("zyy");
		behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SIGN);
		behaviorEntity.setOutBusinessNo("20240725");        // 重复的 OutBusinessNo 会报错唯一索引冲突，这也是保证幂等的手段，确保不会多记账

		List<String> orderIds = behaviorRebateService.createOrder(behaviorEntity);
		log.info("请求参数：{}", JSON.toJSONString(behaviorEntity));
		log.info("测试结果：{}", JSON.toJSONString(orderIds));
	}
}
