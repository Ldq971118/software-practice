package com.softwarepractice.message;

import com.softwarepractice.message.MessageInterface;
import lombok.Data;

@Data
public class Error implements MessageInterface {
    private Boolean success;
    private Integer code;
    private String errMsg;
    private String data;

    public Error(String errMsg) {
        this.success=false;
        this.code=-2001;
        this.errMsg=errMsg;
        this.data=null;
    }
}
