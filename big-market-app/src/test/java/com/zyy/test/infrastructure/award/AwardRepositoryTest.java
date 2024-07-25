package com.zyy.test.infrastructure.award;

import com.zyy.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.zyy.domain.award.model.entity.TaskEntity;
import com.zyy.domain.award.model.entity.UserAwardRecordEntity;
import com.zyy.domain.award.model.valobj.AwardStateVO;
import com.zyy.domain.award.model.valobj.TaskStateVO;
import com.zyy.domain.award.repository.IAwardRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardRepositoryTest {

	@Resource
	private IAwardRepository awardRepository;

	@Test
	public void test_saveUserAwardRecord() {
		UserAwardRecordAggregate userAwardRecordAggregate = new UserAwardRecordAggregate();

		UserAwardRecordEntity userAwardRecordEntity = UserAwardRecordEntity.builder()
			.userId("zjl")
			.activityId(100301L)
			.strategyId(100006L)
			.orderId("111")
			.awardId(101)
			.awardTitle("101").
			awardTime(new Date())
			.awardState(AwardStateVO.create)
			.build();

		TaskEntity taskEntity = TaskEntity.builder()
			.userId("zjl")
			.topic("111")
			.messageId("0")
			.message(null)
			.state(TaskStateVO.create)
			.build();


		userAwardRecordAggregate.setUserAwardRecordEntity(userAwardRecordEntity);
		userAwardRecordAggregate.setTaskEntity(taskEntity);
		awardRepository.saveUserAwardRecord(userAwardRecordAggregate);
	}
}
