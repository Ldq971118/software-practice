package com.softwarepractice.entity;

import lombok.Data;

@Data
public class RepairReply {
    Integer id;
    Integer reply_id;
    String content;
    Integer reply_type=0;
    Integer repair_id;
    String reply_time;
}
