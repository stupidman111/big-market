package com.zyy.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.zyy.domain.activity.service.ISkuStock;
import com.zyy.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ActivitySkuStockZeroCustomer {

	@Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
	private String topic;

	@Resource
	private ISkuStock skuStock;

	/**
	 * 监听 【库存为 0】的消息，执行 【清空数据库库存】 操作
	 * @param message
	 */
	@RabbitListener(queuesToDeclare = @Queue(value = "activity_sku_stock_zero"))
	public void listener(String message) {
		try {
			log.info("监听活动 sku库存消息消耗为 0 的消息 topic:{}, message:{}", topic, message);

			BaseEvent.EventMessage<Long> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<Long>>() {
			}.getType());
			Long sku = eventMessage.getData();
			skuStock.clearActivitySkuStock(sku);//清空数据库库存
			skuStock.clearQueueValue();//清空队列
		} catch (Exception e) {
			log.error("监听活动 sku库存消息消耗为 0 的消息，消费失败，topic:{}, message:{}", topic, message);
			throw e;
		}

	}
}
