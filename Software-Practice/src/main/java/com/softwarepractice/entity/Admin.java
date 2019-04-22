package com.softwarepractice.entity;

import lombok.Data;

/**
 * CREATE TABLE `admin` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `w_id` int(11) NOT NULL COMMENT '员工编号',
 *   `password` varchar(16) NOT NULL COMMENT '密码',
 *   `jurisdirction` int(1) NOT NULL COMMENT '管辖区',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Admin {
    private Integer id;
    private Integer w_id;
    private String password;
    private Integer jurisdirction;
}
