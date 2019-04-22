package com.softwarepractice.entity;

import lombok.Data;

import java.util.Date;

/**
 * CREATE TABLE `fee` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
 *   `d_id` int(11) NOT NULL COMMENT '宿舍编号',
 *   `last_month_quantity` float NOT NULL COMMENT '上月读数',
 *   `current_month_quantity` float NOT NULL COMMENT '本月读数',
 *   `state_time` date NOT NULL COMMENT '费用起始时间',
 *   `end_time` date NOT NULL COMMENT '费用终止时间',
 *   `unit_price` float NOT NULL COMMENT '费用单价',
 *   `free_quantity` float NOT NULL COMMENT '减免额度',
 *   `amount` float NOT NULL COMMENT '应付金额',
 *   `status` int(1) NOT NULL COMMENT '缴费状态 0：未缴费 1：已缴费',
 *   `type` int(1) NOT NULL COMMENT '费用类型 0：水费 1：电费',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
public class Fee {
    private Integer id;
    private Integer d_id;
    private Float last_month_quantity;
    private Float current_month_quantity;
    private Date state_time;
    private Date end_time;
    private Float unit_price;
    private Float free_quantity;
    private Float amount;
    private Integer status;
    private Integer type;

}
