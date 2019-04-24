package com.softwarepractice.message.domitory;

import com.softwarepractice.message.MessageInterface;
import lombok.Data;

@Data
public class AccommendationMessage implements MessageInterface {
    private Integer id; //Accommendation_id
    private String name;
    private Integer stuId; //学号
    private Integer tel;
    private Integer zone;
    private Integer building;
    private Integer room;
}
