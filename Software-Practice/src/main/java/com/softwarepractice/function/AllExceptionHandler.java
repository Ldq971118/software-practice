package com.softwarepractice.function;

import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice()
public class AllExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public MessageInterface myExceptionHandler(Exception t) {
        ErrorMessage errorMessage=new ErrorMessage("系统错误");
        if(t.getMessage()==null);
//        else if (t.getMessage().equals("Num Error"))
//            errorMessage = new ErrorMessage("参数[pageNum]错误，不能是负数");
//        else if (t.getMessage().equals("Token Error"))
//            errorMessage = new ErrorMessage("Token错误");
        else
            errorMessage.setErrMsg(t.getMessage());
        return errorMessage;
    }
}
