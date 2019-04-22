package com.softwarepractice.entity;

import lombok.Data;

/**
 * CREATE TABLE `push` (
 * `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 * `s_id` int(11) NOT NULL COMMENT '学生编号',
 * `form_id` varchar(11) NOT NULL COMMENT '推送编号',
 * PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Push {
    private Integer id;
    private Integer s_id;
    private String form_id;
}
