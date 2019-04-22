package com.softwarepractice.entity;

import lombok.Data;

/**
 * CREATE TABLE `option` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `quantion_id` int(11) NOT NULL COMMENT '问题编号',
 *   `text` varchar(32) NOT NULL COMMENT '选项内容',
 *   `select_number` int(11) NOT NULL COMMENT '已选人数',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Option {
    private Integer id;
    private Integer quantion_id;
    private String text;
    private Integer select_number;
}
