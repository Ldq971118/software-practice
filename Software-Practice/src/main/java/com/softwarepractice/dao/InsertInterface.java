package com.softwarepractice.dao;

import com.softwarepractice.entity.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InsertInterface {
    Integer InsertWorker(Worker worker);
    Integer InsertQuestionnaire(Questionnaire questionnaire);
    Integer InsertQuestion(Question question);
    Integer InsertOption(Options option);
    Integer InsertAccommendation(Accommendation accommendation);
    Integer InsertStudent(Student student);
    Integer InsertInformation(Information information);
}
