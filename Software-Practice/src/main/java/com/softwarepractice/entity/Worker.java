package com.softwarepractice.entity;

import lombok.Data;

/**
 * CREATE TABLE `worker` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `name` varchar(32) NOT NULL COMMENT '姓名',
 *   `worker_id` int(11) NOT NULL COMMENT '工号',
 *   `telephone` int(11) DEFAULT NULL COMMENT '电话',
 *   `type` varchar(16) NOT NULL COMMENT '类别',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Worker {
    private Integer id;
    private String name;
    private Integer worker_id;
    private Integer telephone;
    private String type;
}
