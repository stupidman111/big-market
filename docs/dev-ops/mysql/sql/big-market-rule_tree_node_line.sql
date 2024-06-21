USE big_market;

DROP TABLE IF EXISTS `rule_tree_node_line`;

CREATE TABLE `rule_tree_node_line` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `tree_id` varchar(32) NOT NULL COMMENT '规则树ID',
    `rule_node_from` varchar(32) NOT NULL COMMENT '规则Key节点 From',
    `rule_node_to` varchar(32) NOT NULL COMMENT '规则Key节点 To',
    `rule_limit_type` varchar(8) NOT NULL COMMENT '限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围];',
    `rule_limit_value` varchar(32) NOT NULL COMMENT '限定值（到下个节点）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `rule_tree_node_line` WRITE;
/*!40000 ALTER TABLE `rule_tree_node_line` DISABLE KEYS */;

INSERT INTO `rule_tree_node_line` (`id`, `tree_id`, `rule_node_from`, `rule_node_to`, `rule_limit_type`, `rule_limit_value`, `create_time`, `update_time`)
VALUES
    (1,'tree_lock_1','rule_lock','rule_stock','EQUAL','ALLOW','2024-02-15 07:37:31','2024-02-15 07:55:08'),
    (2,'tree_lock_1','rule_lock','rule_luck_award','EQUAL','TAKE_OVER','2024-02-15 07:37:31','2024-02-15 07:55:11'),
    (3,'tree_lock_1','rule_stock','rule_luck_award','EQUAL','ALLOW','2024-02-15 07:37:31','2024-02-15 07:55:13'),
    (4,'tree_luck_award','rule_stock','rule_luck_award','EQUAL','ALLOW','2024-02-15 07:37:31','2024-02-15 07:39:28'),
    (5,'tree_lock_2','rule_lock','rule_stock','EQUAL','ALLOW','2024-02-15 07:37:31','2024-02-15 07:55:08'),
    (6,'tree_lock_2','rule_lock','rule_luck_award','EQUAL','TAKE_OVER','2024-02-15 07:37:31','2024-02-15 07:55:11'),
    (7,'tree_lock_2','rule_stock','rule_luck_award','EQUAL','ALLOW','2024-02-15 07:37:31','2024-02-15 07:55:13');

/*!40000 ALTER TABLE `rule_tree_node_line` ENABLE KEYS */;
UNLOCK TABLES;