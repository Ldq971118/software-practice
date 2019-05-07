package com.softwarepractice.message.medium;

import com.softwarepractice.entity.Dormitory;
import lombok.Data;

@Data
public class feeMessage {
    private Integer id;
    private String start_time;
    private String end_time;
    private Float amount;
    private Integer status;
    private Integer type;
    private Dormitory dorm;
}
