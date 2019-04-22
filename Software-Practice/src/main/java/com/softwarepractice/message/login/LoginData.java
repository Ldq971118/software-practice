package com.softwarepractice.message.login;

import lombok.Data;

@Data
public class LoginData {
    private String token;
    private AdminInfo adminInfo;

    public LoginData(String token, Integer id, Integer workerId, Integer jurisdiction) {
        this.token = token;
        this.adminInfo=new AdminInfo(id, workerId, jurisdiction);
    }
}
