package com.softwarepractice.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.dao.UpdateInterface;
import com.softwarepractice.entity.Information;
import com.softwarepractice.entity.Worker;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import com.softwarepractice.message.medium.InformationsMessage;
import com.softwarepractice.message.medium.Informations_Worker;
import com.softwarepractice.message.success.SuccessMessage;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/web/information")
@Controller
public class InformationManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/getAllInformations", method = RequestMethod.GET)
    @ResponseBody
    public PageInfo<InformationsMessage> GetAllInformations(Integer pageNum, Integer pageSize, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("header");
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
            session.close();
            return informationsMessagePageInfo;
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
