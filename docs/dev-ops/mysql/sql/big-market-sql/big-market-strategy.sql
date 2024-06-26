/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE database if NOT EXISTS `big_market` default character set utf8mb4 collate utf8mb4_0900_ai_ci;

use `big_market`;

DROP TABLE IF EXISTS `strategy`;

CREATE TABLE `strategy` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id` bigint(8) NOT NULL COMMENT '抽奖策略ID',
    `strategy_desc` varchar(128) NOT NULL COMMENT '抽奖策略描述',
    `rule_models` varchar(256) DEFAULT NULL COMMENT '规则模型，rule配置的模型同步到此表，便于使用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id` (`strategy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `strategy` WRITE;
/*!40000 ALTER TABLE `strategy` DISABLE KEYS */;

INSERT INTO `strategy` (`id`, `strategy_id`, `strategy_desc`, `rule_models`, `create_time`, `update_time`)
VALUES
    (1,100001,'抽奖策略','rule_blacklist,rule_weight','2023-12-09 09:37:19','2024-01-20 11:39:23'),
    (2,100003,'抽奖策略-验证lock','rule_blacklist','2024-01-13 10:34:06','2024-01-20 15:03:19'),
    (3,100002,'抽奖策略-非完整1概率',NULL,'2023-12-09 09:37:19','2024-02-03 10:14:17'),
    (4,100004,'抽奖策略-随机抽奖',NULL,'2023-12-09 09:37:19','2024-01-20 19:21:03'),
    (5,100005,'抽奖策略-测试概率计算',NULL,'2023-12-09 09:37:19','2024-01-21 21:54:58'),
    (6,100006,'抽奖策略-规则树',NULL,'2024-02-03 09:53:40','2024-02-03 09:53:40');

/*!40000 ALTER TABLE `strategy` ENABLE KEYS */;
UNLOCK TABLES;