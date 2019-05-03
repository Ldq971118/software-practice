package com.softwarepractice.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.DeleteInterface;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Worker;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import com.softwarepractice.message.medium.Response;
import com.softwarepractice.message.success.SuccessMessage;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/web/worker")
@CrossOrigin
public class WorkerManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/getAllWorkers", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface GetAllWorkers(Integer pageNum, Integer pageSize, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("token");
            if (!Token.varify(token))
                throw new Exception("Token Error");
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<Worker> workerList = selectInterface.FindWorkerAll();
            PageInfo<Worker> workerPageInfo = new PageInfo<>(workerList);
            Response response=new Response(workerPageInfo);
            session.close();
            return response;
        }
    }

    @RequestMapping(value = "/saveWorker", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface AddWorkers(@RequestBody Map<String, Object> worker_map, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Integer jurisdirction = Token.GetJurisdirction(token);

        ErrorMessage fail=new ErrorMessage("权限不足");
        SuccessMessage success=new SuccessMessage();

        if (jurisdirction != 0)
            return fail;
        else {
            //init worker
            Worker worker = new Worker();
            worker.setName((String) worker_map.get("name"));
            worker.setWorker_id((Integer) worker_map.get("workerId"));
            worker.setTelephone((String) worker_map.get("tel"));
            worker.setType((String) worker_map.get("type"));

            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            InsertInterface insertInterface = session.getMapper(InsertInterface.class);
            Integer effect=insertInterface.InsertWorker(worker);
            session.commit();
            session.close();
            if(effect!=1){
                fail.setErrMsg("添加失败");
                return fail;
            }
            else
                return success;
        }
    }

    @RequestMapping(value = "/removeById", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface DeleteWorker(Integer id,HttpServletRequest request) throws Exception{
        String token = request.getHeader("token");
        Integer jurisdirction = Token.GetJurisdirction(token);

        ErrorMessage fail=new ErrorMessage("权限不足");
        SuccessMessage success=new SuccessMessage();

        if (jurisdirction != 0)
            return fail;
        else {
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            DeleteInterface deleteInterface = session.getMapper(DeleteInterface.class);
            Integer effect=deleteInterface.DeleteWorkerById(id);
            session.commit();
            session.close();

            if(effect!=1){
                fail.setCode(404);
                fail.setErrMsg("找不到对应的员工id");
                return fail;
            }
            else
                return success;
        }
    }
}
