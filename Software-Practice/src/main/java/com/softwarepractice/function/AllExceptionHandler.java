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
        Error error = new Error("系统错误");
        if (t.getMessage() == null) {
        } else if (t.getMessage().equals("Num Error")) {
            error = new Error("参数pageNum或pageSize错误");
        } else if (t.getMessage().equals("Token Error")) {
            error = new Error("Token非法");
        } else if(t.getMessage().equals("Change Fail")){
            error = new Error("修改失败");
        }else if(t.getMessage().equals("No Exist")){
            error = new Error("修改内容不存在");
        }else if(t.getMessage().equals("Permission Denied")){
            error = new Error("权限不足，需要超级管理员权限");
        }else if(t.getMessage().equals("Add Fail")){
            error = new Error("添加失败");
        }else if(t.getMessage().equals("Upload Fail")){
            error = new Error("文件上传失败");
        }else if(t.getMessage().equals("Import Data Fail")){
            error = new Error("数据导入失败");
        }else {}
        return error;
    }
}
