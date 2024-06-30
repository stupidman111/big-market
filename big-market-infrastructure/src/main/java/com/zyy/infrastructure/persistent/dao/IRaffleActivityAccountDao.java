package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.RaffleActivityAccount;
import com.zyy.infrastructure.persistent.po.RaffleActivityOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityAccountDao {
	int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);

	void insert(RaffleActivityAccount raffleActivityAccount);

}
