/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE database if NOT EXISTS `big_market` default character set utf8mb4 collate utf8mb4_0900_ai_ci;

USE big_market;

DROP TABLE IF EXISTS `user_behavior_rebate_order`;

CREATE TABLE `user_behavior_rebate_order` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id` varchar(32) NOT NULL COMMENT '用户ID',
    `order_id` varchar(12) NOT NULL COMMENT '订单ID',
    `behavior_type` varchar(16) NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc` varchar(128) NOT NULL COMMENT '返利描述',
    `rebate_type` varchar(16) NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config` varchar(32) NOT NULL COMMENT '返利配置【sku值，积分值】',
    `biz_id` varchar(64) NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为返利流水订单表';


LOCK TABLES `user_behavior_rebate_order` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order` DISABLE KEYS */;

INSERT INTO `user_behavior_rebate_order` (`id`, `user_id`, `order_id`, `behavior_type`, `rebate_desc`, `rebate_type`, `rebate_config`, `biz_id`, `create_time`, `update_time`)
VALUES
    (1,'xiaofuge','833814327101','sign','签到返利','sku','9011','xiaofuge_sku_20240430','2024-04-30 18:01:32','2024-04-30 18:01:32'),
    (3,'xiaofuge','509399206701','sign','签到返利-积分','integral','10','xiaofuge_integral_20240430','2024-04-30 18:05:44','2024-04-30 18:05:44');

/*!40000 ALTER TABLE `user_behavior_rebate_order` ENABLE KEYS */;
UNLOCK TABLES;


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;