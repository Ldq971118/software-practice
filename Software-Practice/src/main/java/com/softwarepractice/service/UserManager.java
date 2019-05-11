package com.softwarepractice.service;


import com.alibaba.fastjson.JSONObject;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Admin;
import com.softwarepractice.entity.Worker;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.Error;
import com.softwarepractice.message.login.LoginSuccess;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequestMapping("/api/web/user")
@CrossOrigin
public class UserManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    MessageInterface login(@RequestBody Map<String, Object> map) throws Exception {
        String username_str = (String) map.get("username");
        String passwd = (String) map.get("password");
        LoginSuccess loginSuccess;
        Error loginError;

        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        SelectInterface selectInterface = session.getMapper(SelectInterface.class);
        Integer username = Integer.parseInt(username_str);
        Admin admin = selectInterface.selectAdminByWorkerId(username);
        session.close();

        if (admin != null && admin.getPassword().equals(passwd)) {
            loginSuccess = new LoginSuccess(Token.createToken(username, passwd, admin.getJurisdirction()), admin.getId(),
                    username, admin.getJurisdirction());
            return loginSuccess;
        } else {
            loginError = new Error("用户名或密码错误");
            return loginError;
        }
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public @ResponseBody
    JSONObject getUserInfo(HttpServletRequest request) throws Exception {
        String token=request.getHeader("token");
        Integer workerId = Token.getUsername(token);
        Integer jurisdiction=Token.getJurisdirction(token);
        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        SelectInterface selectInterface = session.getMapper(SelectInterface.class);
        Worker worker=selectInterface.selectWorkerByWorkerId(workerId);
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
