package com.softwarepractice.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.dao.UpdateInterface;
import com.softwarepractice.entity.Dormitory;
import com.softwarepractice.entity.Information;
import com.softwarepractice.entity.Worker;
import com.softwarepractice.function.PostInformation;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import com.softwarepractice.message.medium.InformationsMessage;
import com.softwarepractice.message.medium.Informations_Worker;
import com.softwarepractice.message.medium.Response;
import com.softwarepractice.message.success.SuccessMessage;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/web/information")
@Controller
@CrossOrigin
public class InformationManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/postInformation", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface postInformation(@RequestBody Map<String,Object> data_map,HttpServletRequest request) throws Exception{
        String token = request.getHeader("token");
        if (!Token.varify(token))
            throw new Exception("Token Error");

        Integer zone=(Integer) data_map.get("zone");

        Information information=new Information();
        information.setTime((String) data_map.get("time"));
        information.setContent((String) data_map.get("content"));
        information.setTitle((String) data_map.get("title"));
        information.setBuilding((Integer) data_map.get("building"));
        information.setRoom((Integer) data_map.get("room"));
        information.setW_id((Integer) data_map.get("workerId"));
        information.setZone(zone);

        SqlSession sqlSession = sqlSessionFactoryBean.getObject().openSession();

        PostInformation postInformation=new PostInformation();
        Dormitory dormitory=new Dormitory();
        boolean mark=true;
        if(zone.equals(0)){}
        else{
            dormitory.setZone(zone);
            dormitory.setBuilding((Integer) data_map.get("building"));
            dormitory.setRoom((Integer) data_map.get("room"));
            SelectInterface selectInterface=sqlSession.getMapper(SelectInterface.class);
            dormitory=selectInterface.SelectDormitory(dormitory);
            if(dormitory==null){
                ErrorMessage errorMessage=new ErrorMessage("没有找到该宿舍");
                return errorMessage;
            }
            mark=false;
        }
        InsertInterface insertInterface=sqlSession.getMapper(InsertInterface.class);
        Integer effect=insertInterface.InsertInformation(information);
        sqlSession.commit();
        sqlSession.close();
        if(effect!=1){
            ErrorMessage errorMessage=new ErrorMessage("推送失败");
            return errorMessage;
        }else{
            SuccessMessage successMessage=new SuccessMessage();
            postInformation.getAccess_token();
            if(mark)
                postInformation.PostAll(information);
            else
                postInformation.PostOneDorm(dormitory.getId(),information);
            return successMessage;
        }
    }



    @RequestMapping(value = "/getAllInformations", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface GetAllInformations(Integer pageNum, Integer pageSize, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("token");
            if (!Token.varify(token))
                throw new Exception("Token Error");
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            List<Information> informationList=selectInterface.FindInformationAll();
            List<InformationsMessage> informationsMessageList=new ArrayList<>();
            for(int i=0;i<informationList.size();i++){
                Worker worker=selectInterface.SelectWorker(informationList.get(i).getW_id());
                InformationsMessage informationsMessage=new InformationsMessage();
                informationsMessage.setId(informationList.get(i).getId());
                informationsMessage.setTitle(informationList.get(i).getTitle());
                informationsMessage.setTime(informationList.get(i).getTime());
                informationsMessage.setZone(informationList.get(i).getZone());
                informationsMessage.setBuilding(informationList.get(i).getBuilding());
                informationsMessage.setRoom(informationList.get(i).getRoom());
                informationsMessage.setContent(informationList.get(i).getContent());
                informationsMessage.setW_id(informationList.get(i).getW_id());
                Informations_Worker informations_worker=new Informations_Worker();
                informations_worker.setId(worker.getId());
                informations_worker.setName(worker.getName());
                informations_worker.setTelephone(worker.getTelephone());
                informationsMessage.setWorker(informations_worker);
                informationsMessageList.add(informationsMessage);
            }

            PageHelper.startPage(pageNum, pageSize);
            PageInfo<InformationsMessage> informationsMessagePageInfo =
                    new PageInfo<>(informationsMessageList);
            Response response=new Response(informationsMessagePageInfo);
            session.close();
            return response;
        }
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface UpdateInformation(@RequestBody Map<String,Object> data_map, HttpServletRequest request) throws Exception{
        ErrorMessage fail=new ErrorMessage("权限不足");
        SuccessMessage successMessage=new SuccessMessage();

        String token = request.getHeader("token");
        if (!Token.varify(token))
            throw new Exception("Token Error");
        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        SelectInterface selectInterface = session.getMapper(SelectInterface.class);
        Information information=selectInterface.SelectInformation((Integer)data_map.get("id"));
        Integer jurisdirction=Token.GetJurisdirction(token);
        if(jurisdirction==0||information.getZone().equals(jurisdirction)){
            information.setTitle((String) data_map.get("title"));
            information.setContent((String) data_map.get("content"));
            information.setTime((String) data_map.get("time"));
            UpdateInterface updateInterface=session.getMapper(UpdateInterface.class);
            updateInterface.UpdateInformation(information);
            session.commit();
            session.close();
            return successMessage;
        }else{
            session.close();
            return fail;
        }
    }
}
