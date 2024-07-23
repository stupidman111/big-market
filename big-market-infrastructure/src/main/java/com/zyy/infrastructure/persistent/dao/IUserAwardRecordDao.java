package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.UserAwardRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserAwardRecordDao {
	void insert(UserAwardRecord userAwardRecord);

}
