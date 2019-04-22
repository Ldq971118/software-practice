package com.softwarepractice.entity;

import lombok.Data;

/**
 * CREATE TABLE `accommendation` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `s_id` int(11) NOT NULL COMMENT '学生编号',
 *   `d_id` int(11) NOT NULL COMMENT '宿舍编号',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Accommendation {
    private Integer id;
    private Integer s_id;
    private Integer d_id;
}
