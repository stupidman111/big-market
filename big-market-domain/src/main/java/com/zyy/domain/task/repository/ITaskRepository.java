package com.zyy.domain.task.repository;

import com.zyy.domain.task.model.entity.TaskEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ITaskRepository {
	List<TaskEntity> queryNoSendMessageTaskList();

	void sendMessage(TaskEntity taskEntity);

	void updateTaskSendMessageCompleted(String userId, String messageId);

	void updateTaskSendMessageFail(String userId, String messageId);

}
