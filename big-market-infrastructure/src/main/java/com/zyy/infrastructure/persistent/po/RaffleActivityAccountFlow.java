package com.zyy.infrastructure.persistent.po;

import lombok.Data;
import org.springframework.boot.SpringApplication;

import java.util.Date;

@Data
public class RaffleActivityAccountFlow {

	/** 自增 ID **/
	private Integer id;
	/** 用户 ID **/
	private String userId;
	/** 活动 ID **/
	private Long activityId;
	/** 总次数 **/
	private Integer totalCount;
	/** 日次数 **/
	private Integer dayCount;
	/** 月次数 **/
	private Integer monthCount;
	/** 流水 ID - 生成的唯一 ID **/
	private String flowId;
	/** 流水渠道 （activity-活动领取、sale-购买、redeem-兑换、free-免费赠送）**/
	private String flowChannel;
	/** 业务 ID **/
	private String bizId;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;
}
