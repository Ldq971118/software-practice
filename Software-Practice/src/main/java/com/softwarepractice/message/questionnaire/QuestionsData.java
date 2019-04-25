package com.softwarepractice.message.questionnaire;

import lombok.Data;

import java.util.List;

@Data
public class QuestionsData {
    private String text;
    List<OptionsData> options;
}
