package com.softwarepractice.message.medium;

import lombok.Data;

@Data
public class ComplaintMessage {
    private Integer id;
    private Integer s_id;
    private Integer student_id;
    private String telephone;
    private String content;
    private String picture;
    private Integer status;
    private String time;
    private String update_time;
}
