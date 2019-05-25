package com.softwarepractice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.dao.UpdateInterface;
import com.softwarepractice.entity.Repair;
import com.softwarepractice.entity.RepairReply;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.Success;
import com.softwarepractice.message.medium.RepairMessage;
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
@RequestMapping("/api/web/repair")
@CrossOrigin
public class RepairManager {
    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/getAllRepairs", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getAllRepairs(Integer pageNum, Integer pageSize, HttpServletRequest request) throws Exception {
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
            List<RepairMessage> repairList = selectInterface.findRepairAll(Token.getJurisdirction(token));
            PageInfo<RepairMessage> repairPageInfo = new PageInfo<>(repairList);
            Response response = new Response(repairPageInfo);
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
        Integer effect = updateInterface.updateRepairStatus(id, status);
        if (effect != 1) {
            throw new Exception("Change Fail");
        } else {
            session.commit();
            session.close();
            Success success = new Success();
            return success;
        }
    }

    @RequestMapping(value = "/repairReply", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface repairReply(@RequestBody String content, Integer id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        if (!Token.varify(token)) {
            throw new Exception("Token Error");
        }
        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        SelectInterface selectInterface = session.getMapper(SelectInterface.class);
        InsertInterface insertInterface = session.getMapper(InsertInterface.class);
        Repair repair = selectInterface.
                selectRepairByIdAndZone(id, Token.getJurisdirction(token));
        if (repair == null) {
            throw new Exception("No Exist");
        } else {
            RepairReply repairReply = new RepairReply();
            repairReply.setR_id(repair.getId());
            repairReply.setReplycontent(content);
            insertInterface.insertRepairReply(repairReply);
        }
        Success success = new Success();
        return success;
    }
}
