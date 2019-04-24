package com.softwarepractice.message.medium;

import com.softwarepractice.message.MessageInterface;
import lombok.Data;

@Data
public class AccommendationMessage implements MessageInterface {
    private Integer id; //Accommendation_id
    private String name;
    private Integer tel;
    private Integer zone;
    private Integer building;
    private Integer room;
}
