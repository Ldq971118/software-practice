package com.softwarepractice.message.medium;

import com.softwarepractice.message.MessageInterface;
import lombok.Data;

@Data
public class Response implements MessageInterface {

    private Boolean success;
    private Integer code;
    private String errMsg;
    private Object data;

    public Response(Object data) {
        this.success = true;
        this.code = 200;
        this.errMsg = "";
        this.data = data;
    }
}
