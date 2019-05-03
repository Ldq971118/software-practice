package com.softwarepractice.controller;


import com.alibaba.fastjson.JSONObject;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Admin;
import com.softwarepractice.entity.Worker;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import com.softwarepractice.message.login.LoginSuccess;
import com.softwarepractice.message.success.SuccessMessage;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/api/web/user")
@CrossOrigin
public class UserManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    MessageInterface Login(@RequestBody Map<String, Object> map) throws Exception {
        String username_str = (String) map.get("username");
        String passwd = (String) map.get("password");
        LoginSuccess loginSuccess;
        ErrorMessage loginError;

        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        SelectInterface selectInterface = session.getMapper(SelectInterface.class);
        Integer username = Integer.parseInt(username_str);
        Admin admin = selectInterface.SelectAdmin(username);
        session.close();

        if (admin != null && admin.getPassword().equals(passwd)) {
            loginSuccess = new LoginSuccess(Token.createToken(username, passwd, admin.getJurisdirction()), admin.getId(),
                    username, admin.getJurisdirction());
            return loginSuccess;
        } else {
            loginError = new ErrorMessage("用户名或密码错误");
            return loginError;
        }
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public @ResponseBody
    JSONObject getUserInfo(HttpServletRequest request) throws Exception {
        String token=request.getHeader("token");
        Integer workerId = Token.GetUsername(token);
        Integer jurisdiction=Token.GetJurisdirction(token);
        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        SelectInterface selectInterface = session.getMapper(SelectInterface.class);
        Worker worker=selectInterface.SelectWorker(workerId);
        session.close();
        JSONObject jsonData=new JSONObject();
        jsonData.put("workerId",worker.getWorker_id());
        jsonData.put("telephone",worker.getTelephone());
        jsonData.put("jurisdiction",jurisdiction);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("success",true);
        jsonObject.put("code",200);
        jsonObject.put("errMsg","");
        jsonObject.put("data",jsonData);
        return jsonObject;
    }

}
