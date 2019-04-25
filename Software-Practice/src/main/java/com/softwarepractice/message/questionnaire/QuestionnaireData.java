package com.softwarepractice.message.questionnaire;

import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireData {
    private Integer id;
    private String title;
    private String start_time;
    private String end_time;
    private Integer workerId;
    private Integer zone;
    private Integer building;
    private Integer room;
    private List<QuestionsData> questions;
}
