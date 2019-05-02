package com.softwarepractice.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Fee;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.fee.feeMessage;
import com.softwarepractice.message.medium.Response;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/api/web/fee")
@CrossOrigin
public class FeeManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/getAllFees", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getAllFees(Integer pageNum, Integer pageSize, Integer type, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("token");
            if (!Token.varify(token))
                throw new Exception("Token Error");
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<feeMessage> feeList = selectInterface.FindFeeAll(Token.GetJurisdirction(token));
            PageInfo<feeMessage> feePageInfo = new PageInfo<>(feeList);
            Response response=new Response(feePageInfo);
            session.close();
            return response;
        }
    }

    @RequestMapping(value = "/getWaterFees", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getWaterFees(Integer pageNum, Integer pageSize, Integer type, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("token");
            if (!Token.varify(token))
                throw new Exception("Token Error");
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<feeMessage> feeList = selectInterface.FindFeeByType(Token.GetJurisdirction(token),type);
            PageInfo<feeMessage> feePageInfo = new PageInfo<>(feeList);
            Response response=new Response(feePageInfo);
            session.close();
            return response;
        }
    }

    @RequestMapping(value = "/getElectricityFees", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getElectricityFees(Integer pageNum, Integer pageSize, Integer type, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("token");
            if (!Token.varify(token))
                throw new Exception("Token Error");
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<feeMessage> feeList = selectInterface.FindFeeByType(Token.GetJurisdirction(token),type);
            PageInfo<feeMessage> feePageInfo = new PageInfo<>(feeList);
            Response response=new Response(feePageInfo);
            session.close();
            return response;
        }
    }
}
