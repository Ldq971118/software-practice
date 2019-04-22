package com.softwarepractice.entity;

import lombok.Data;

/**
 * CREATE TABLE `dormitory` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `zone` int(11) NOT NULL COMMENT '区号',
 *   `building` int(11) NOT NULL COMMENT '楼号',
 *   `room` int(11) NOT NULL COMMENT '房号',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Dormitory {
    private Integer id;
    private Integer zone;
    private Integer building;
    private Integer room;
}
