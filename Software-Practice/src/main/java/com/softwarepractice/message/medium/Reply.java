package com.softwarepractice.message.medium;

import lombok.Data;

@Data
public class Reply {
    private String content;
    private String reply_time;
    private Integer reply_type;
}
