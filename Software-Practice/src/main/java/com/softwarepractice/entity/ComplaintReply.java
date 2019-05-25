package com.softwarepractice.entity;

import lombok.Data;

@Data
public class ComplaintReply {
    Integer id;
    Integer reply_id;
    String content;
    Integer reply_type=0;
    Integer complaint_id;
    String reply_time;
}
