package com.softwarepractice.message.success;

import com.softwarepractice.message.MessageInterface;
import lombok.Data;

@Data
public class SuccessMessage implements MessageInterface {
    private Boolean success;
    private Integer code;
    private String errMsg;
    private String data;

    public SuccessMessage() {
        this.success=true;
        this.code=200;
        this.errMsg="";
        this.data=null;
    }
}
