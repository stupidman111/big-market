package com.zyy.test.infrastructure.activity;

import com.zyy.infrastructure.persistent.dao.IRaffleActivityAccountDao;
import com.zyy.infrastructure.persistent.dao.IRaffleActivityOrderDao;
import com.zyy.infrastructure.persistent.po.RaffleActivityAccount;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivityAccountTest {

	@Resource
	private IRaffleActivityAccountDao raffleActivityAccountDao;

	@Test
	public void test_insert() {
		RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();

		raffleActivityAccount.setUserId("zy");
		raffleActivityAccount.setActivityId(100301L);
		raffleActivityAccount.setTotalCount(10);
		raffleActivityAccount.setTotalCountSurplus(10);
		raffleActivityAccount.setMonthCount(10);
		raffleActivityAccount.setMonthCountSurplus(10);
		raffleActivityAccount.setDayCount(10);
		raffleActivityAccount.setDayCountSurplus(10);

		raffleActivityAccountDao.insert(raffleActivityAccount);
	}

	/**
	 *         update big_market.raffle_activity_account
	 *         set
	 *             total_count = total_count + #{totalCount},
	 *             total_count_surplus = total_count_surplus + #{totalCountSurplus},
	 *             month_count = month_count + #{monthCount},
	 *             month_count_surplus = month_count_surplus + #{monthCountSurplus},
	 *             day_count = day_count + #{dayCount},
	 *             day_count_surplus = day_count_surplus + #{dayCountSurplus},
	 *             update_time = now()
	 *         where user_id = #{userId} and activity_id = #{activityId};
	 */
	@Test
	public void test_updateAccountQuota() {
		RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();

		raffleActivityAccount.setUserId("zy");
		raffleActivityAccount.setActivityId(100301L);
		raffleActivityAccount.setTotalCount(11);
		raffleActivityAccount.setTotalCountSurplus(11);
		raffleActivityAccount.setMonthCount(11);
		raffleActivityAccount.setMonthCountSurplus(11);
		raffleActivityAccount.setDayCount(11);
		raffleActivityAccount.setDayCountSurplus(11);

		raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
	}
}
