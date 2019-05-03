package com.softwarepractice.entity;

import lombok.Data;

/**
 * CREATE TABLE `question` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `questionnaire_id` int(11) NOT NULL COMMENT '问卷编号',
 *   `content` varchar(32) NOT NULL COMMENT '选项内容',
 *   `type` int(1) NOT NULL COMMENT '选项类型 0：单选 1：多选',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Question {
    private Integer id;
    private Integer questionnaire_id;
    private String content;
    private Integer type;
}
