package com.softwarepractice.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Worker;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/web/worker")
public class WorkerManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/getAllWorkers",method = RequestMethod.GET)
    @ResponseBody
    public PageInfo<Worker> GetAllWorkers(Integer pageNum, Integer pageSize) throws Exception{
        if(pageNum<0||pageSize<=0){
            throw new Exception("Num Error");
        }
        else{
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum,pageSize);
            List<Worker> workerList=selectInterface.FindWorkerAll();
            PageInfo<Worker> workerPageInfo=new PageInfo<>(workerList);
            session.close();
            return workerPageInfo;
        }
    }
}
