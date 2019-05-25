package com.softwarepractice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.dao.UpdateInterface;
import com.softwarepractice.entity.Complaint;
import com.softwarepractice.entity.ComplaintReply;
import com.softwarepractice.function.Token;

import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.Success;
import com.softwarepractice.message.medium.ComplaintMessage;
import com.softwarepractice.message.Response;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
@RequestMapping("/api/web/complaint")
@CrossOrigin
public class ComplaintManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/getAllComplaints", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getAllComplaints(Integer pageNum, Integer pageSize, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0) {
            throw new Exception("Num Error");
        } else {
            String token = request.getHeader("token");
            if (!Token.varify(token)) {
                throw new Exception("Token Error");
            }
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<ComplaintMessage> complaintList = selectInterface.findComplaintAll(Token.getJurisdirction(token));
            PageInfo<ComplaintMessage> complaintPageInfo = new PageInfo<>(complaintList);
            Response response = new Response(complaintPageInfo);
            session.close();
            return response;
        }
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface changeStatus(Integer id, Integer status, HttpServletRequest request) throws Exception {

        String token = request.getHeader("token");
        if (!Token.varify(token)) {
            throw new Exception("Token Error");
        }
        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        UpdateInterface updateInterface = session.getMapper(UpdateInterface.class);
        Integer effect = updateInterface.updateComplaintStatus(id, status);
        session.commit();
        session.close();
        if (effect != 1) {
            throw new Exception("Change Fail");
        } else {
            Success success = new Success();
            return success;
        }
    }

    @RequestMapping(value = "/complaintReply", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface complaintReply(@RequestBody String content, Integer id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        if (!Token.varify(token)) {
            throw new Exception("Token Error");
        }
        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        SelectInterface selectInterface = session.getMapper(SelectInterface.class);
        InsertInterface insertInterface = session.getMapper(InsertInterface.class);
        Complaint complaint = selectInterface.
                selectComplaintByIdAndZone(id, Token.getJurisdirction(token));
        if (complaint == null) {
            throw new Exception("No Exist");
        } else {
            ComplaintReply complaintReply = new ComplaintReply();
            complaintReply.setC_id(complaint.getId());
            complaintReply.setReplycontent(content);
            insertInterface.insertComplaintReply(complaintReply);
        }
        Success success = new Success();
        return success;
    }
}
