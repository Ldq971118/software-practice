package com.softwarepractice.entity;

import lombok.Data;


/**
 * CREATE TABLE `repair` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `d_id` int(11) NOT NULL COMMENT '宿舍编号',
 *   `s_id` int(11) NOT NULL COMMENT '学生编号',
 *   `telephone` int(11) DEFAULT NULL COMMENT '电话',
 *   `content` varchar(255) NOT NULL COMMENT '内容',
 *   `picture` varchar(32) DEFAULT NULL COMMENT '图片 url',
 *   `status` int(1) NOT NULL COMMENT '处理状态 0：未受理 1：已受理 2：已完成',
 *   `time` datetime NOT NULL,
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Repair {
    private Integer id;
    private Integer d_id;
    private Integer s_id;
    private String telephone;
    private String content;
    private String picture;
    private Integer status;
    private String time;
    private String update_time;
}
