/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50639
 Source Host           : localhost:3306
 Source Schema         : road-map

 Target Server Type    : MySQL
 Target Server Version : 50639
 File Encoding         : 65001

 Date: 15/07/2023 09:26:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE database if NOT EXISTS `big_market` default character set utf8mb4 collate utf8mb4_0900_ai_ci;
use `big_market`;

-- ----------------------------
-- Table structure for `award` --奖品表
-- ----------------------------
DROP TABLE IF EXISTS `award`;
CREATE TABLE `award` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `award_id` int(8) NOT NULL COMMENT '抽奖奖品ID - 内部流转使用',
    `award_key` varchar(32) NOT NULL COMMENT '奖品对接标识 - 对应到发奖策略',
    `award_config` varchar(32) NOT NULL COMMENT '奖品配置信息',
    `award_desc` varchar(128) NOT NULL COMMENT '奖品内容描述',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) # 主键
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of `award`
-- ----------------------------
LOCK TABLES `award` WRITE; # 对表加写锁
# BEGIN;
INSERT INTO `award` (`id`, `award_id`, `award_key`, `award_config`, `award_desc`)
VALUES
    (1, 101, 'user_credit_random', '1,100', '用户积分【优先透彻规则范围，如果没有则走配置】'),
    (2, 102, 'openai_use_count', '5', 'OpenAI 增加使用次数'),
    (3, 103, 'openai_use_count', '10', 'OpenAI 增加使用次数'),
    (4, 104, 'openai_use_count', '20', 'OpenAI 增加使用次数'),
    (5, 105, 'openai_model', 'gpt-4', 'OpenAI 增加模型'),
    (6, 106, 'openai_model', 'dall-e-2', 'OpenAI 增加模型'),
    (7, 107, 'openai_model', 'dall-e-3', 'OpenAI 增加模型'),
    (8, 108, 'openai_use_count', '100', 'OpenAI增加使用次数'),
    (9, 109, 'openai_model', 'gpt-4,dall-e-2,dall-e-3', 'OpenAI增加模型');
# COMMIT;
UNLOCK TABLES;

-- ----------------------------
-- Table structure for `strategy` --策略表
-- ----------------------------
DROP TABLE IF EXISTS `strategy`;

CREATE TABLE `strategy` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id` bigint(8) NOT NULL COMMENT '抽奖策略ID',
    `strategy_desc` varchar(128) NOT NULL COMMENT '抽奖策略描述',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id` (`strategy_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of `strategy`
-- ----------------------------
LOCK TABLES `strategy` WRITE;

INSERT INTO `strategy` (`id`, `strategy_id`, `strategy_desc`, `create_time`, `update_time`)
VALUES
    (1,100001,'抽奖策略','2023-12-09 09:37:19','2023-12-09 09:37:19');
UNLOCK TABLES;


-- ----------------------------
-- Table structure for `strategy_award` --策略-奖品对应表
-- ----------------------------
DROP TABLE IF EXISTS `strategy_award`;
CREATE TABLE `strategy_award` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id` bigint(8) NOT NULL COMMENT '抽奖策略ID',
    `award_id` int(8) NOT NULL COMMENT '抽奖奖品ID - 内部流转使用',
    `award_title` varchar(128) NOT NULL COMMENT '抽奖奖品标题',
    `award_subtitle` varchar(128) DEFAULT NULL COMMENT '抽奖奖品副标题',
    `award_count` int(8) NOT NULL DEFAULT '0' COMMENT '奖品库存总量',
    `award_count_surplus` int(8) NOT NULL DEFAULT '0' COMMENT '奖品库存剩余',
    `award_rate` decimal(6, 4) NOT NULL COMMENT '奖品中奖概率',
    `rule_models` varchar(256) DEFAULT NULL COMMENT '规则模型, rule配置的模型同步到此，便于使用',
    `sort` int(2) NOT NULL DEFAULT '0' COMMENT '排序',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id_award_id` (`strategy_id`, `award_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of `strategy_award`
-- ----------------------------
BEGIN;
INSERT INTO `strategy_award` (`id`, `strategy_id`, `award_id`, `award_title`, `award_subtitle`, `award_count`, `award_count_surplus`, `award_rate`, `rule_models`, `sort`)
VALUES
    (1, 100001, 101, '随机积分', NULL, 80000, 80000, 80.000, 'rule_random,rule_luck_award', 1),
    (2, 100001, 102, '5次使用', NULL, 10000, 10000, 10.000, 'rule_luck_award', 2),
    (3, 100001, 103, '10次使用', NULL, 5000, 5000, 5.000, 'rule_luck_award', 3),
    (4, 100001, 104, '20次使用', NULL, 4000, 4000, 4.000, 'rule_luck_award', 4),
    (5, 100001, 105, '增加gpt-4对话模型', NULL, 600, 600, 0.600, 'rule_luck_award', 5),
    (6, 100001, 106, '增加dall-e-2画图模型', NULL, 150, 150, 0.150, 'rule_luck_award', 6),
    (7, 100001, 107, '增加dall-e-3画图模型', '抽奖1次后解锁', 150, 150, 0.150, 'rule_lock,rule_luck_award', 7),
    (8, 100001, 108, '增加100次使用','抽奖2次后解锁', 90, 90, 0.090, 'rule_lock,rule_luck_award', 8),
    (9, 100001, 109, '解锁全部模型','抽奖6次后解锁', 10, 10, 0.010, 'rule_lock,rule_luck_award', 9);
COMMIT;

-- ----------------------------
-- Table structure for `strategy_rule` --策略规则表
-- ----------------------------
DROP TABLE IF EXISTS `strategy_rule`;
CREATE TABLE `strategy_rule` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id` int(8) NOT NULL COMMENT '抽奖策略ID',
    `award_id` int(8) DEFAULT NULL COMMENT '抽奖奖品ID【规则类型为策略，则不需要奖品ID】',
    `rule_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '抽象规则类型；1-策略规则、2-奖品规则',
    `rule_model` varchar(16) NOT NULL COMMENT '抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】',
    `rule_value` varchar(64) NOT NULL COMMENT '抽奖规则比值',
    `rule_desc` varchar(128) NOT NULL COMMENT '抽奖规则描述',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id_award_id` (`strategy_id`,`award_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of `strategy_rule`
-- ----------------------------
LOCK TABLES `strategy_rule` WRITE;

INSERT INTO `strategy_rule` (`id`, `strategy_id`, `award_id`, `rule_type`, `rule_model`, `rule_value`, `rule_desc`, `create_time`, `update_time`)
VALUES
	(1,100001,101,2,'rule_random','1,1000','随机积分策略','2023-12-09 10:05:30','2023-12-09 12:55:52'),
	(2,100001,107,2,'rule_lock','1','抽奖1次后解锁','2023-12-09 10:16:41','2023-12-09 12:55:53'),
	(3,100001,108,2,'rule_lock','2','抽奖2次后解锁','2023-12-09 10:17:43','2023-12-09 12:55:54'),
	(4,100001,109,2,'rule_lock','6','抽奖6次后解锁','2023-12-09 10:17:43','2023-12-09 12:55:54'),
	(5,100001,107,2,'rule_luck_award','1,100','兜底奖品100以内随机积分','2023-12-09 10:30:12','2023-12-09 12:55:55'),
	(6,100001,108,2,'rule_luck_award','1,100','兜底奖品100以内随机积分','2023-12-09 10:30:43','2023-12-09 12:55:56'),
	(7,100001,101,2,'rule_luck_award','1,10','兜底奖品10以内随机积分','2023-12-09 10:30:43','2023-12-09 12:55:57'),
	(8,100001,102,2,'rule_luck_award','1,20','兜底奖品20以内随机积分','2023-12-09 10:30:43','2023-12-09 12:55:57'),
	(9,100001,103,2,'rule_luck_award','1,30','兜底奖品30以内随机积分','2023-12-09 10:30:43','2023-12-09 12:55:58'),
	(10,100001,104,2,'rule_luck_award','1,40','兜底奖品40以内随机积分','2023-12-09 10:30:43','2023-12-09 12:55:59'),
	(11,100001,105,2,'rule_luck_award','1,50','兜底奖品50以内随机积分','2023-12-09 10:30:43','2023-12-09 12:56:00'),
	(12,100001,106,2,'rule_luck_award','1,60','兜底奖品60以内随机积分','2023-12-09 10:30:43','2023-12-09 12:56:00'),
	(13,100001,NULL,1,'rule_weight','6000,102,103,104,105,106,107,108,109','消耗6000分，必中奖范围','2023-12-09 10:30:43','2023-12-09 12:58:21'),
	(14,100001,NULL,1,'rule_blacklist','1','黑名单抽奖，积分兜底','2023-12-09 12:59:45','2023-12-09 13:42:23');
UNLOCK TABLES;


