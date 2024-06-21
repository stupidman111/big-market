USE big_market;

DROP TABLE IF EXISTS `rule_tree`;

CREATE TABLE `rule_tree` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `tree_id` varchar(32) NOT NULL COMMENT '规则树ID',
    `tree_name` varchar(64) NOT NULL COMMENT '规则树名称',
    `tree_desc` varchar(128) DEFAULT NULL COMMENT '规则树描述',
    `tree_node_rule_key` varchar(32) NOT NULL COMMENT '规则树根入口规则',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_tree_id` (`tree_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



LOCK TABLES `rule_tree` WRITE;
/*!40000 ALTER TABLE `rule_tree` DISABLE KEYS */;

INSERT INTO `rule_tree` (`id`, `tree_id`, `tree_name`, `tree_desc`, `tree_node_rule_key`, `create_time`, `update_time`)
VALUES
    (1,'tree_lock_1','规则树','规则树','rule_lock','2024-01-27 10:01:59','2024-02-15 07:49:59'),
    (2,'tree_luck_award','规则树-兜底奖励','规则树-兜底奖励','rule_stock','2024-02-15 07:35:06','2024-02-15 07:50:20'),
    (3,'tree_lock_2','规则树','规则树','rule_lock','2024-01-27 10:01:59','2024-02-15 07:49:59');

/*!40000 ALTER TABLE `rule_tree` ENABLE KEYS */;
UNLOCK TABLES;