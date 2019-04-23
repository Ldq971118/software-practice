package com.softwarepractice.message.login;

import com.softwarepractice.message.MessageInterface;
import lombok.Data;

@Data
public class LoginSuccess implements MessageInterface {

    private Boolean success;
    private Integer code;
    private String errMsg;
    private LoginData Data;

    public LoginSuccess(String token,Integer id, Integer workerId, Integer jurisdiction) {
        this.success = true;
        this.code = 200;
        this.errMsg = "";
        this.Data=new LoginData(token, id, workerId, jurisdiction);
    }
}
