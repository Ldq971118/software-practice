package com.example.softwarepractice.controller;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/web/user")
public class UserLogin {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public void Login(@RequestBody Map<String,Object> map){
        String username=(String)map.get("username");
        String passwd=(String)map.get("password");
        SqlSession session=sqlSessionFactory.openSession();

    }
}
