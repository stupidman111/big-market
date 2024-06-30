/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE database if NOT EXISTS `big_market` default character set utf8mb4 collate utf8mb4_0900_ai_ci;

USE big_market;

DROP TABLE IF EXISTS `raffle_activity_account`;

/********* 抽奖活动账户表 *********/
CREATE TABLE `raffle_activity_account`(
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id` varchar(32) NOT NULL COMMENT '用户ID',
    `activity_id` bigint(12) NOT NULL COMMENT '活动ID',
    `total_count` int(8) NOT NULL COMMENT '总次数',
    `total_count_surplus` int(8) NOT NULL COMMENT '总次数-剩余',
    `day_count` int(8) NOT NULL COMMENT '日次数',
    `day_count_surplus` int(8) NOT NULL COMMENT '日次数-剩余',
    `month_count` int(8) NOT NULL COMMENT '月次数',
    `month_count_surplus` int(8) NOT NULL COMMENT '月次数-剩余',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id` (`user_id`,`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖活动账户表';



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;