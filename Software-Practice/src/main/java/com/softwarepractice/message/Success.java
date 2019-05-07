package com.softwarepractice.message;

import com.softwarepractice.message.MessageInterface;
import lombok.Data;

@Data
public class Success implements MessageInterface {
    private Boolean success;
    private Integer code;
    private String errMsg;
    private String data;

    public Success() {
        this.success=true;
        this.code=200;
        this.errMsg="";
        this.data=null;
    }
}
