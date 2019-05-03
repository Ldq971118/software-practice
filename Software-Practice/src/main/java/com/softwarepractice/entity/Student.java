package com.softwarepractice.entity;

import lombok.Data;

/**
 *CREATE TABLE `student` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `name` varchar(32) NOT NULL COMMENT '姓名',
 *   `student_id` int(11) NOT NULL COMMENT '学号',
 *   `telephone` int(11) DEFAULT NULL COMMENT '电话',
 *   `open_id` varchar(16) DEFAULT NULL COMMENT '微信ID，由微信提供，在首次登陆时绑定',
 *   `is_leader` int(1) DEFAULT NULL COMMENT '是否舍长 0：舍长 1：舍员',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Student {
    private Integer id;
    private String name;
    private Integer student_id;
    private String telephone;
    private String open_id;
    private Integer is_leader;
}
