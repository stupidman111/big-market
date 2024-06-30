/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE database if NOT EXISTS `big_market` default character set utf8mb4 collate utf8mb4_0900_ai_ci;

USE big_market;

DROP TABLE IF EXISTS `raffle_activity_order`;

/********* 用户抽奖活动单（用户领取活动后，会自动生成一条订单，对应该表的一条数据） *********/
CREATE TABLE `raffle_activity_order` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id` varchar(32) NOT NULL COMMENT '用户ID',
    `sku` bigint(12) NOT NULL COMMENT '商品sku',
    `activity_id` bigint(12) NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
    `strategy_id` bigint(8) NOT NULL COMMENT '抽奖策略ID',
    `order_id` varchar(12) NOT NULL COMMENT '订单ID',
    `order_time` datetime NOT NULL COMMENT '下单时间',
    `total_count` int(8) NOT NULL COMMENT '总次数',
    `day_count` int(8) NOT NULL COMMENT '日次数',
    `month_count` int(8) NOT NULL COMMENT '月次数',
    `state` varchar(8) NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`,`activity_id`,`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖活动单';

LOCK TABLES `raffle_activity_order` WRITE;
/*!40000 ALTER TABLE `raffle_activity_order` DISABLE KEYS */;

INSERT INTO `raffle_activity_order` (`id`, `user_id`, `sku`, `activity_id`, `activity_name`, `strategy_id`, `order_id`, `order_time`, `total_count`, `day_count`, `month_count`, `state`, `create_time`, `update_time`)
VALUES
    (1,'ODRhfGEfX',0,100301,'测试活动',100006,'826130522615','2024-03-09 06:26:20',0,0,0,'not_used','2024-03-09 14:26:19','2024-03-09 14:26:19'),
    (2,'IoTtOmcBeivNUYv',0,100301,'测试活动',100006,'469450480489','2024-03-09 06:26:20',0,0,0,'not_used','2024-03-09 14:26:20','2024-03-09 14:26:20');

/*!40000 ALTER TABLE `raffle_activity_order` ENABLE KEYS */;
UNLOCK TABLES;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;