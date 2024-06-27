/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE database if NOT EXISTS `big_market` default character set utf8mb4 collate utf8mb4_0900_ai_ci;

USE big_market;

DROP TABLE IF EXISTS `raffle_activity`;

CREATE TABLE `raffle_activity` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `activity_id` bigint(12) NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
    `activity_desc` varchar(128) NOT NULL COMMENT '活动描述',
    `begin_date_time` datetime NOT NULL COMMENT '开始时间',
    `end_date_time` datetime NOT NULL COMMENT '结束时间',
    `stock_count` int(11) NOT NULL COMMENT '库存总量',
    `stock_count_surplus` int(11) NOT NULL COMMENT '剩余库存',
    `activity_count_id` bigint(12) NOT NULL COMMENT '活动参与次数配置（去到另一张表中查）',
    `strategy_id` bigint(8) NOT NULL COMMENT '抽奖策略ID',
    `state` varchar(8) NOT NULL COMMENT '活动状态',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_activity_id` (`activity_id`),
    KEY `idx_begin_date_time` (`begin_date_time`),
    KEY `idx_end_date_time` (`end_date_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖活动表';

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;