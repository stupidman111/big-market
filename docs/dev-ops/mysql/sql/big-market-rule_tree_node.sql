USE big_market;

DROP TABLE IF EXISTS `rule_tree_node`;

CREATE TABLE `rule_tree_node` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `tree_id` varchar(32) NOT NULL COMMENT '规则树ID',
    `rule_key` varchar(32) NOT NULL COMMENT '规则Key',
    `rule_desc` varchar(64) NOT NULL COMMENT '规则描述',
    `rule_value` varchar(128) DEFAULT NULL COMMENT '规则比值',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `rule_tree_node` WRITE;
/*!40000 ALTER TABLE `rule_tree_node` DISABLE KEYS */;

INSERT INTO `rule_tree_node` (`id`, `tree_id`, `rule_key`, `rule_desc`, `rule_value`, `create_time`, `update_time`)
VALUES
    (1,'tree_lock_1','rule_lock','限定用户已完成N次抽奖后解锁','1','2024-01-27 10:03:09','2024-02-15 07:50:57'),
    (2,'tree_lock_1','rule_luck_award','兜底奖品随机积分','101:1,100','2024-01-27 10:03:09','2024-02-15 07:51:00'),
    (3,'tree_lock_1','rule_stock','库存扣减规则',NULL,'2024-01-27 10:04:43','2024-02-15 07:51:02'),
    (4,'tree_luck_award','rule_stock','库存扣减规则',NULL,'2024-02-15 07:35:55','2024-02-15 07:39:19'),
    (5,'tree_luck_award','rule_luck_award','兜底奖品随机积分','101:1,100','2024-02-15 07:35:55','2024-02-15 07:39:23'),
    (6,'tree_lock_2','rule_lock','限定用户已完成N次抽奖后解锁','2','2024-01-27 10:03:09','2024-02-15 07:52:20'),
    (7,'tree_lock_2','rule_luck_award','兜底奖品随机积分','101:1,100','2024-01-27 10:03:09','2024-02-08 19:59:43'),
    (8,'tree_lock_2','rule_stock','库存扣减规则',NULL,'2024-01-27 10:04:43','2024-02-03 10:40:21');

/*!40000 ALTER TABLE `rule_tree_node` ENABLE KEYS */;
UNLOCK TABLES;