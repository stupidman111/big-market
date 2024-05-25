package com.zyy.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "redis.sdk.config", ignoreInvalidFields = true)
public class RedisClientConfigProperties {

	/** host **/
	private String host;
	/** 端口 **/
	private int port;
	/** 密码 **/
	private String password;
	/** 连接池大小，默认为64 **/
	private int poolSize = 64;
	/** 连接池的最小空闲连接数目 **/
	private int minIdleSize = 10;
	/** 连接的最大空闲时间 **/
	private int idleTimeout = 10000;
	/** 连接超时时间 **/
	private int connectTimeout = 10000;
	/** 连接重试次数 **/
	private int retryAttempts = 3;
	/** 连接重试时间间隔 **/
	private int retryInteval = 1000;
	/** 定期检查连接是否可用的时间间隔 **/
	private int pingInterval = 0;
	/** 是否长连接 **/
	private boolean keepAlive = true;
}
