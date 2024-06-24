/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE database if NOT EXISTS `big_market` default character set utf8mb4 collate utf8mb4_0900_ai_ci;

use `big_market`;

DROP TABLE IF EXISTS `strategy_award`;

CREATE TABLE `strategy_award` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id` bigint(8) NOT NULL COMMENT '抽奖策略ID',
    `award_id` int(8) NOT NULL COMMENT '抽奖奖品ID - 内部流转使用',
    `award_title` varchar(128) NOT NULL COMMENT '抽奖奖品标题',
    `award_subtitle` varchar(128) DEFAULT NULL COMMENT '抽奖奖品副标题',
    `award_count` int(8) NOT NULL DEFAULT '0' COMMENT '奖品库存总量',
    `award_count_surplus` int(8) NOT NULL DEFAULT '0' COMMENT '奖品库存剩余',
    `award_rate` decimal(6,4) NOT NULL COMMENT '奖品中奖概率',
    `rule_models` varchar(256) DEFAULT NULL COMMENT '规则模型，rule配置的模型同步到此表，便于使用',
    `sort` int(2) NOT NULL DEFAULT '0' COMMENT '排序',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id_award_id` (`strategy_id`,`award_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `strategy_award` WRITE;
/*!40000 ALTER TABLE `strategy_award` DISABLE KEYS */;

INSERT INTO `strategy_award` (`id`, `strategy_id`, `award_id`, `award_title`, `award_subtitle`, `award_count`, `award_count_surplus`, `award_rate`, `rule_models`, `sort`, `create_time`, `update_time`)
VALUES
    (1,100001,101,'随机积分',NULL,80000,80000,0.3000,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:09'),
    (2,100001,102,'5次使用',NULL,10000,10000,0.2000,'tree_luck_award',2,'2023-12-09 09:39:18','2024-02-15 07:42:11'),
    (3,100001,103,'10次使用',NULL,5000,5000,0.2000,'tree_luck_award',3,'2023-12-09 09:42:36','2024-02-15 07:42:12'),
    (4,100001,104,'20次使用',NULL,4000,4000,0.1000,'tree_luck_award',4,'2023-12-09 09:43:15','2024-02-15 07:42:12'),
    (5,100001,105,'增加gpt-4对话模型',NULL,600,600,0.1000,'tree_luck_award',5,'2023-12-09 09:43:47','2024-02-15 07:42:13'),
    (6,100001,106,'增加dall-e-2画图模型',NULL,200,200,0.0500,'tree_luck_award',6,'2023-12-09 09:44:20','2024-02-15 07:42:14'),
    (7,100001,107,'增加dall-e-3画图模型','抽奖1次后解锁',200,200,0.0400,'tree_luck_award',7,'2023-12-09 09:45:38','2024-02-15 07:42:17'),
    (8,100001,108,'增加100次使用','抽奖2次后解锁',199,199,0.0099,'tree_luck_award',8,'2023-12-09 09:46:02','2024-02-15 07:42:21'),
    (9,100001,109,'解锁全部模型','抽奖6次后解锁',1,1,0.0001,'tree_luck_award',9,'2023-12-09 09:46:39','2024-02-15 07:42:26'),
    (10,100002,101,'随机积分',NULL,1,1,0.5000,'tree_luck_award',1,'2023-12-09 09:46:39','2024-02-15 07:42:29'),
    (11,100002,102,'5次使用',NULL,1,1,0.1000,'tree_luck_award',2,'2023-12-09 09:46:39','2024-02-15 07:42:32'),
    (12,100002,106,'增加dall-e-2画图模型',NULL,1,1,0.0100,'tree_luck_award',3,'2023-12-09 09:46:39','2024-02-15 07:42:35'),
    (13,100003,107,'增加dall-e-3画图模型','抽奖1次后解锁',200,200,0.0400,'tree_luck_award',7,'2023-12-09 09:45:38','2024-02-15 07:42:38'),
    (14,100003,108,'增加100次使用','抽奖2次后解锁',199,199,0.0099,'tree_luck_award',8,'2023-12-09 09:46:02','2024-02-15 07:42:41'),
    (15,100003,109,'解锁全部模型','抽奖6次后解锁',1,1,0.0001,'tree_luck_award',9,'2023-12-09 09:46:39','2024-02-15 07:42:44'),
    (16,100004,109,'解锁全部模型','抽奖6次后解锁',1,1,1.0000,'tree_luck_award',9,'2023-12-09 09:46:39','2024-02-15 07:42:46'),
    (17,100005,101,'随机积分',NULL,80000,80000,0.0300,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:47'),
    (18,100005,102,'随机积分',NULL,80000,80000,0.0300,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:48'),
    (19,100005,103,'随机积分',NULL,80000,80000,0.0300,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:50'),
    (20,100005,104,'随机积分',NULL,80000,80000,0.0300,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:51'),
    (21,100005,105,'随机积分',NULL,80000,80000,0.0010,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:52'),
    (22,100006,101,'随机积分',NULL,100,88,0.0200,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 12:34:20'),
    (23,100006,102,'7等奖',NULL,100,62,0.0300,'tree_luck_award',2,'2023-12-09 09:38:31','2024-02-15 12:34:25'),
    (24,100006,103,'6等奖',NULL,100,71,0.0300,'tree_luck_award',3,'2023-12-09 09:38:31','2024-02-15 12:34:30'),
    (25,100006,104,'5等奖',NULL,100,68,0.0300,'tree_luck_award',4,'2023-12-09 09:38:31','2024-02-15 12:34:10'),
    (26,100006,105,'4等奖',NULL,100,74,0.0300,'tree_luck_award',5,'2023-12-09 09:38:31','2024-02-15 12:32:45'),
    (27,100006,106,'3等奖','抽奖1次后解锁',100,68,0.0300,'tree_lock_1',6,'2023-12-09 09:38:31','2024-02-15 12:34:00'),
    (28,100006,107,'2等奖','抽奖1次后解锁',100,72,0.0300,'tree_lock_1',7,'2023-12-09 09:38:31','2024-02-15 12:33:50'),
    (29,100006,108,'1等奖','抽奖2次后解锁',100,74,0.0300,'tree_lock_2',8,'2023-12-09 09:38:31','2024-02-15 12:32:55');

/*!40000 ALTER TABLE `strategy_award` ENABLE KEYS */;
UNLOCK TABLES;