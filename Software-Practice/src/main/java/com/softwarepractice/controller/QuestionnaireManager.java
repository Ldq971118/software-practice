package com.softwarepractice.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Options;
import com.softwarepractice.entity.Question;
import com.softwarepractice.entity.Questionnaire;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import com.softwarepractice.message.success.SuccessMessage;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/web/questionnaires")
public class QuestionnaireManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/postQuestionnaire", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface AddQuestionnaire(String params) throws Exception{
        ErrorMessage fail=new ErrorMessage("问卷发布失败");
        SuccessMessage success=new SuccessMessage();
        JSONObject jsonObject= JSONObject.parseObject(params);
        //basic information
        String title=jsonObject.getString("title");
        Date startTime=jsonObject.getDate("startTime");
        Date endTime=jsonObject.getDate("endTime");
        Integer workerId=jsonObject.getInteger("workerId");
        Integer zong=jsonObject.getInteger("zone");
        Integer building=jsonObject.getInteger("building");
        Integer room=jsonObject.getInteger("room");

        Questionnaire questionnaire=new Questionnaire();
        questionnaire.setTitle(title);
        questionnaire.setStart_time(startTime);
        questionnaire.setEnd_time(endTime);
        questionnaire.setW_id(workerId);
        questionnaire.setZone(zong);
        questionnaire.setBuilding(building);
        questionnaire.setRoom(room);

        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        InsertInterface insertInterface = session.getMapper(InsertInterface.class);
        Integer questionnaire_effect=insertInterface.InsertQuestionnaire(questionnaire);
        if(questionnaire_effect!=1)
            return fail;
        //questions
        Integer questionnarie_id=questionnaire.getId();
        JSONArray jsonlist=jsonObject.getJSONArray("list");
        Question question;
        Options option;
        for(int i=0;i<jsonlist.size();i++){
            //get question
            question=new Question();
            question.setQuestionnarie_id(questionnarie_id);
            question.setContent(jsonlist.getJSONObject(i).getString("title"));
            question.setType(jsonlist.getJSONObject(i).getInteger("type"));
            Integer question_effect=insertInterface.InsertQuestion(question);
            if(question_effect!=1)
                return fail;
            Integer question_id=question.getId();
            //get options
            JSONArray optionArray=jsonlist.getJSONObject(i).getJSONArray("options");
            //填空题没有options
            if(optionArray==null)
                continue;
            else{
                for(int j=0;j<optionArray.size();j++){
                    option=new Options();
                    option.setQuestion_id(question_id);
                    option.setSelect_number(0);
                    option.setContent(optionArray.getJSONObject(j).getString("content"));
                    Integer option_effect=insertInterface.InsertOption(option);
                    if(option_effect!=1)
                        return fail;
                }
            }
        }
        session.commit();
        session.close();
        return success;
    }

}
