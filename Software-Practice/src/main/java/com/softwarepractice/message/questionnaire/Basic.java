package com.softwarepractice.message.questionnaire;

import com.softwarepractice.message.MessageInterface;
import lombok.Data;

@Data
public class Basic implements MessageInterface {
    boolean success;
    Integer code;
    String errMsg;
    QuestionnaireData data;


}
