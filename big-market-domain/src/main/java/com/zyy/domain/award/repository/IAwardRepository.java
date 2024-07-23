package com.zyy.domain.award.repository;

import com.zyy.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.springframework.stereotype.Repository;

public interface IAwardRepository {
	void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);

}
