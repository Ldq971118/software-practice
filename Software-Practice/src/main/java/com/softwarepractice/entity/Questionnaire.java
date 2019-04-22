package com.softwarepractice.entity;

import lombok.Data;

import java.util.Date;

/**
 * CREATE TABLE `questionnaire` (
 * `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 * `title` varchar(32) NOT NULL COMMENT '标题',
 * `start_time` date NOT NULL COMMENT '起始时间',
 * `end_time` date NOT NULL COMMENT '结束时间',
 * `w_id` int(11) NOT NULL COMMENT '发送员工编号',
 * `zone` int(11) DEFAULT NULL COMMENT '区号',
 * `building` int(11) DEFAULT NULL COMMENT '楼号',
 * `room` int(11) DEFAULT NULL COMMENT '房号',
 * PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Questionnaire {
    private Integer id;
    private String title;
    private Date start_time;
    private Date end_time;
    private Integer w_id;
    private Integer zone;
    private Integer building;
    private Integer room;
}
