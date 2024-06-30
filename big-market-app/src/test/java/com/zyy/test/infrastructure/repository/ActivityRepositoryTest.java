package com.zyy.test.infrastructure.repository;

import com.zyy.domain.activity.repository.IActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityRepositoryTest {

	@Resource
	private IActivityRepository activityRepository;

	@Test
	public void test_queryRaffleActivityCountByActivityCountId() {
		//activityRepository.queryRaffleActivityCountByActivityCountId();
	}
}
