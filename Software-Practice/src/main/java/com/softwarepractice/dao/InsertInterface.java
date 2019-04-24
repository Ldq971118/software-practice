package com.softwarepractice.dao;

import com.softwarepractice.entity.Options;
import com.softwarepractice.entity.Question;
import com.softwarepractice.entity.Questionnaire;
import com.softwarepractice.entity.Worker;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InsertInterface {
    Integer InsertWorker(Worker worker);
    Integer InsertQuestionnaire(Questionnaire questionnaire);
    Integer InsertQuestion(Question question);
    Integer InsertOption(Options option);
}
