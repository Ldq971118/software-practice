package com.softwarepractice.entity;

import lombok.Data;

/**
 * CREATE TABLE `options` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `quextion_id` int(11) NOT NULL COMMENT '问题编号',
 *   `content` varchar(32) NOT NULL COMMENT '选项内容',
 *   `select_number` int(11) NOT NULL COMMENT '已选人数',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Options {
    private Integer id;
    private Integer question_id;
    private String content;
    private Integer select_number;
}
