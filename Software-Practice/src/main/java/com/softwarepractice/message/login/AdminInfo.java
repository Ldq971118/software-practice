package com.softwarepractice.message.login;

import lombok.Data;

@Data
public class AdminInfo {
    private Integer id;
    private Integer workerId;
    private Integer jurisdiction;

    public AdminInfo(Integer id, Integer workerId, Integer jurisdiction) {
        this.id = id;
        this.workerId = workerId;
        this.jurisdiction = jurisdiction;
    }
}
