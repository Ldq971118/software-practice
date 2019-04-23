package com.softwarepractice.controller;


import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Admin;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import com.softwarepractice.message.login.LoginSuccess;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/web/user")
public class UserLogin {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    MessageInterface Login(@RequestBody Map<String, Object> map) throws Exception {
        String username_str = (String) map.get("username");
        String passwd = (String) map.get("password");
//        System.out.println(username_str);
//        System.out.println(passwd);
        LoginSuccess loginSuccess;
        ErrorMessage loginError;

        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        SelectInterface selectInterface = session.getMapper(SelectInterface.class);
        Integer username = Integer.parseInt(username_str);
        Admin admin = selectInterface.SelectAdmin(username);
        session.close();

        if (admin != null && admin.getPassword().equals(passwd)) {
            loginSuccess = new LoginSuccess(Token.createToken(username, passwd), admin.getId(),
                    admin.getW_id(), admin.getJurisdirction());
            return loginSuccess;
        } else {
            loginError = new ErrorMessage("用户名或密码错误");
            return loginError;
        }

    }
}
