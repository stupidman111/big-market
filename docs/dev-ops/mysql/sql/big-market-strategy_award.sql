
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
    (1,100001,101,'随机积分',NULL,80000,80000,0.3000,'rule_random',1,'2023-12-09 09:38:31','2023-12-31 11:14:42'),
    (2,100001,102,'5次使用',NULL,10000,10000,0.2000,'rule_luck_award',2,'2023-12-09 09:39:18','2023-12-23 13:59:56'),
    (3,100001,103,'10次使用',NULL,5000,5000,0.2000,'rule_luck_award',3,'2023-12-09 09:42:36','2023-12-23 14:00:00'),
    (4,100001,104,'20次使用',NULL,4000,4000,0.1000,'rule_luck_award',4,'2023-12-09 09:43:15','2023-12-23 14:00:10'),
    (5,100001,105,'增加gpt-4对话模型',NULL,600,600,0.1000,'rule_luck_award',5,'2023-12-09 09:43:47','2023-12-23 14:00:12'),
    (6,100001,106,'增加dall-e-2画图模型',NULL,200,200,0.0500,'rule_luck_award',6,'2023-12-09 09:44:20','2023-12-23 14:00:58'),
    (7,100001,107,'增加dall-e-3画图模型','抽奖1次后解锁',200,200,0.0400,'rule_lock,rule_luck_award',7,'2023-12-09 09:45:38','2023-12-23 14:01:02'),
    (8,100001,108,'增加100次使用','抽奖2次后解锁',199,199,0.0099,'rule_lock,rule_luck_award',8,'2023-12-09 09:46:02','2023-12-23 14:05:36'),
    (9,100001,109,'解锁全部模型','抽奖6次后解锁',1,1,0.0001,'rule_lock,rule_luck_award',9,'2023-12-09 09:46:39','2023-12-09 12:20:50'),
    (10,100002,101,'随机积分',NULL,1,1,0.5000,'rule_random,rule_luck_award',1,'2023-12-09 09:46:39','2023-12-23 14:23:51'),
    (11,100002,102,'5次使用',NULL,1,1,0.1000,'rule_random,rule_luck_award',2,'2023-12-09 09:46:39','2023-12-23 14:23:52'),
    (12,100002,106,'增加dall-e-2画图模型',NULL,1,1,0.0100,'rule_random,rule_luck_award',3,'2023-12-09 09:46:39','2023-12-23 14:23:53'),
    (13,100003,107,'增加dall-e-3画图模型','抽奖1次后解锁',200,200,0.0400,'rule_lock,rule_luck_award',7,'2023-12-09 09:45:38','2023-12-23 14:01:02'),
    (14,100003,108,'增加100次使用','抽奖2次后解锁',199,199,0.0099,'rule_lock,rule_luck_award',8,'2023-12-09 09:46:02','2024-01-13 10:26:29'),
    (15,100003,109,'解锁全部模型','抽奖6次后解锁',1,1,0.0001,'rule_lock,rule_luck_award',9,'2023-12-09 09:46:39','2023-12-09 12:20:50'),
    (16,100004,109,'解锁全部模型','抽奖6次后解锁',1,1,1.0000,'rule_random',9,'2023-12-09 09:46:39','2024-01-20 19:19:50'),
    (17,100005,101,'随机积分',NULL,80000,80000,0.0300,'rule_random',1,'2023-12-09 09:38:31','2024-01-21 21:56:51'),
    (18,100005,102,'随机积分',NULL,80000,80000,0.0300,'rule_random',1,'2023-12-09 09:38:31','2024-01-21 22:19:43'),
    (19,100005,103,'随机积分',NULL,80000,80000,0.0300,'rule_random',1,'2023-12-09 09:38:31','2024-01-21 22:19:45'),
    (20,100005,104,'随机积分',NULL,80000,80000,0.0300,'rule_random',1,'2023-12-09 09:38:31','2024-01-21 22:19:48'),
    (21,100005,105,'随机积分',NULL,80000,80000,0.0010,'rule_random',1,'2023-12-09 09:38:31','2024-01-21 22:25:24'),
    (22,100006,101,'随机积分',NULL,3,3,0.0300,'tree_lock',1,'2023-12-09 09:38:31','2024-02-03 11:17:05'),
    (23,100006,102,'随机积分',NULL,97,97,0.9700,'tree_lock',1,'2023-12-09 09:38:31','2024-02-03 11:17:10');

/*!40000 ALTER TABLE `strategy_award` ENABLE KEYS */;
UNLOCK TABLES;