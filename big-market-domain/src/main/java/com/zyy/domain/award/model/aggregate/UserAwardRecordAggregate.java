package com.zyy.domain.award.model.aggregate;

import com.zyy.domain.award.model.entity.TaskEntity;
import com.zyy.domain.award.model.entity.UserAwardRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordAggregate {

	private UserAwardRecordEntity userAwardRecordEntity;

	private TaskEntity taskEntity;
}
