package com.softwarepractice.entity;

import lombok.Data;


/**
 * CREATE TABLE `information` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `title` varchar(32) NOT NULL COMMENT '标题',
 *   `content` varchar(255) NOT NULL COMMENT '内容',
 *   `time` datetime NOT NULL COMMENT '发布时间',
 *   `w_id` int(11) NOT NULL COMMENT '发送员工编号',
 *   `zone` int(11) DEFAULT NULL COMMENT '区号',
 *   `building` int(11) DEFAULT NULL COMMENT '楼号',
 *   `room` int(11) DEFAULT NULL COMMENT '房号',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Information {
    private Integer id;
    private String title;
    private String content;
    private String time;
    private Integer w_id;
    private Integer zone;
    private Integer building;
    private Integer room;
}
