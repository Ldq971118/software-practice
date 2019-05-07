package com.softwarepractice.function;

import com.softwarepractice.message.Error;
import com.softwarepractice.message.MessageInterface;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice()
public class AllExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public MessageInterface myExceptionHandler(Exception t) {
        Error error =new Error("系统错误");
        if(t.getMessage()==null);
//        else if (t.getMessage().equals("Num Error"))
//            error = new Error("参数[pageNum]错误，不能是负数");
//        else if (t.getMessage().equals("Token Error"))
//            error = new Error("Token错误");
        else
            error.setErrMsg(t.getMessage());
        return error;
    }
}
