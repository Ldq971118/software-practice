package com.softwarepractice.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Options;
import com.softwarepractice.entity.Question;
import com.softwarepractice.entity.Questionnaire;
import com.softwarepractice.entity.Worker;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.Error;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.Success;
import com.softwarepractice.message.Response;
import com.softwarepractice.message.questionnaire.Basic;
import com.softwarepractice.message.questionnaire.OptionsData;
import com.softwarepractice.message.questionnaire.QuestionnaireData;
import com.softwarepractice.message.questionnaire.QuestionsData;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service
@RequestMapping("/api/web/questionnaires")
@CrossOrigin
public class QuestionnaireManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/postQuestionnaire", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface addQuestionnaire(@RequestBody String params, HttpServletRequest request) throws Exception {
        Error fail = new Error("问卷发布失败");
        Success success = new Success();

        String token = request.getHeader("token");
        if (!Token.varify(token))
            throw new Exception("Token Error");

        JSONObject jsonObject = JSONObject.parseObject(params);
        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        SelectInterface selectInterface=session.getMapper(SelectInterface.class);
        Worker worker=selectInterface.selectWorkerByWorkerId(jsonObject.getInteger("workerId"));
        if(worker==null){
            Error error =new Error("问卷发布失败");
            return error;
        }

        //basic information
        String title = jsonObject.getString("title");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        Integer w_Id = worker.getId();
        Integer zong = jsonObject.getInteger("zone");
        Integer building = jsonObject.getInteger("building");
        Integer room = jsonObject.getInteger("room");

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTitle(title);
        questionnaire.setStart_time(startTime);
        questionnaire.setEnd_time(endTime);
        questionnaire.setW_id(w_Id);
        questionnaire.setZone(zong);
        questionnaire.setBuilding(building);
        questionnaire.setRoom(room);


        InsertInterface insertInterface = session.getMapper(InsertInterface.class);
        Integer questionnaire_effect = insertInterface.insertQuestionnaire(questionnaire);
        if (questionnaire_effect != 1)
            return fail;

        //questions
        Integer questionnaire_id = questionnaire.getId();
        JSONArray jsonlist = jsonObject.getJSONArray("list");
        Question question;
        Options option;
        for (int i = 0; i < jsonlist.size(); i++) {
            //get question
            question = new Question();
            question.setQuestionnaire_id(questionnaire_id);
            question.setContent(jsonlist.getJSONObject(i).getString("title"));
            question.setType(jsonlist.getJSONObject(i).getInteger("type"));
            Integer question_effect = insertInterface.insertQuestion(question);
            if (question_effect != 1)
                return fail;
            Integer question_id = question.getId();
            //get options
            JSONArray optionArray = jsonlist.getJSONObject(i).getJSONArray("options");
            //填空题没有options
            if (optionArray == null)
                continue;
            else {
                for (int j = 0; j < optionArray.size(); j++) {
                    option = new Options();
                    option.setQuestion_id(question_id);
                    option.setSelect_number(0);
                    option.setContent(optionArray.getJSONObject(j).getString("content"));
                    Integer option_effect = insertInterface.insertOption(option);
                    if (option_effect != 1)
                        return fail;
                }
            }
        }
        session.commit();
        session.close();

        return success;
    }

    @RequestMapping(value = "/getAllQuestionnaires", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getAllQuestionnaire(Integer pageNum, Integer pageSize, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("token");
            if (!Token.varify(token))
                throw new Exception("Token Error");
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<Questionnaire> questionnaireList = selectInterface.findQuestionnaireAll(Token.getJurisdirction(token));
            PageInfo<Questionnaire> questionnairePageInfo = new PageInfo<>(questionnaireList);
            Response response=new Response(questionnairePageInfo);
            session.close();
            return response;
        }
    }

    @RequestMapping(value = "/getQuestionnaireDetail", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getQuestionnaireDetail(Integer id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        if (!Token.varify(token))
            throw new Exception("Token Error");
        SqlSession session = sqlSessionFactoryBean.getObject().openSession();
        SelectInterface selectInterface = session.getMapper(SelectInterface.class);
        Questionnaire questionnaire=selectInterface.selectQuestionnaireById(id);

        //build json

        Basic basicMessage=new Basic();
        basicMessage.setCode(200);
        basicMessage.setSuccess(true);
        basicMessage.setErrMsg("");

        QuestionnaireData questionnaireData=new QuestionnaireData();
        questionnaireData.setId(questionnaire.getId());
        questionnaireData.setStart_time(questionnaire.getStart_time());
        questionnaireData.setEnd_time(questionnaire.getEnd_time());
        questionnaireData.setBuilding(questionnaire.getBuilding());
        questionnaireData.setRoom(questionnaire.getRoom());
        questionnaireData.setTitle(questionnaire.getTitle());
        questionnaireData.setZone(questionnaire.getZone());
        questionnaireData.setWorkerId(questionnaire.getW_id());

        List<Question> questions = selectInterface.findQuestionAllByQuestionaireId(questionnaire.getId());
        List<QuestionsData> questionsData=new ArrayList<>();

        for(int i=0;i<questions.size();i++){
            Question question=questions.get(i);
            QuestionsData questionstemp=new QuestionsData();
            questionstemp.setText(question.getContent());

            List<Options> options=selectInterface.findOptionsAllByQuestionId(question.getId());
            List<OptionsData> optionsData=new ArrayList<>();
            for(int j=0;j<options.size();j++){
                OptionsData optionstemp=new OptionsData();
                optionstemp.setText(options.get(j).getContent());
                optionstemp.setSelectNum(options.get(j).getSelect_number());
                optionsData.add(optionstemp);
            }
            questionstemp.setOptions(optionsData);
            questionsData.add(questionstemp);
        }

        questionnaireData.setQuestions(questionsData);
        basicMessage.setData(questionnaireData);

        session.close();
        return basicMessage;
    }


}
