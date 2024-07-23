package com.zyy.infrastructure.persistent.dao;

import com.zyy.infrastructure.persistent.po.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITaskDao {
	void insert(Task task);

	void updateTaskSendMessageCompleted(Task task);

	void updateTaskSendMessageFail(Task task);

	List<Task> queryNoSendMessageTaskList();

}
