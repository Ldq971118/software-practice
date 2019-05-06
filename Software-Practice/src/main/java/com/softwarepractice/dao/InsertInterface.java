package com.softwarepractice.dao;

import com.softwarepractice.entity.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InsertInterface {
    Integer insertWorker(Worker worker);
    Integer insertQuestionnaire(Questionnaire questionnaire);
    Integer insertQuestion(Question question);
    Integer insertOption(Options option);
    Integer insertAccommendation(Accommendation accommendation);
    Integer insertStudent(Student student);
    Integer insertInformation(Information information);
}
