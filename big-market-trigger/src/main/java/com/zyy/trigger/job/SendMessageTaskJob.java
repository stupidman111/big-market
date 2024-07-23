package com.zyy.trigger.job;


import com.zyy.domain.task.model.entity.TaskEntity;
import com.zyy.domain.task.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component

public class SendMessageTaskJob {

	@Resource
	private ITaskService taskService;

	@Resource
	private ThreadPoolExecutor executor;

	/**
	 * 每 5 秒执行一次定时任务
	 */
	@Scheduled(cron = "0/5 * * * * ?")
	public void exec() {

		/** 查询发送MQ失败和超时1分钟未发送的MQ 消息 **/
		List<TaskEntity> taskEntities = taskService.queryNoSendMessageTaskList();

		if (taskEntities.isEmpty()) return;

		try {
			// 发送MQ消息
			for (TaskEntity taskEntity : taskEntities) {
				// 开启线程发送，提高发送效率。配置的线程池策略为 CallerRunsPolicy，在 ThreadPoolConfig 配置中有4个策略，面试中容易对比提问。可以检索下相关资料。
				executor.execute(() -> {
					try {
						taskService.sendMessage(taskEntity);
						taskService.updateTaskSendMessageCompleted(taskEntity.getUserId(), taskEntity.getMessageId());
					} catch (Exception e) {
						log.error("定时任务，发送MQ消息失败 userId: {} topic: {}", taskEntity.getUserId(), taskEntity.getTopic());
						taskService.updateTaskSendMessageFail(taskEntity.getUserId(), taskEntity.getMessageId());
					}
				});
			}
		} catch (Exception e) {
			log.error("定时任务，扫描MQ任务表发送消息失败。", e);
		}

	}
}
