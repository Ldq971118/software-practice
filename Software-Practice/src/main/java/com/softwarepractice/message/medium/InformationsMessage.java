package com.softwarepractice.message.medium;

import com.softwarepractice.message.MessageInterface;
import lombok.Data;


@Data
public class InformationsMessage implements MessageInterface {
    private Integer id;
    private String title;
    private String content;
    private String time;
    private Integer w_id;
    private Integer zone;
    private Integer building;
    private Integer room;
    private Informations_Worker worker;
}
