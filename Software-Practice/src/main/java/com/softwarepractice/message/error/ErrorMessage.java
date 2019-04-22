package com.softwarepractice.message.error;

import com.softwarepractice.message.MessageInterface;
import lombok.Data;

@Data
public class ErrorMessage implements MessageInterface {
    private Boolean success;
    private Integer code;
    private String errMsg;
    private String data;

    public ErrorMessage(String errMsg) {
        this.success=false;
        this.code=-2001;
        this.errMsg=errMsg;
        this.data=null;
    }
}
