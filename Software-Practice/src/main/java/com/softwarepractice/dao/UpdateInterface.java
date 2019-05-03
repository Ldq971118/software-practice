package com.softwarepractice.dao;

import com.softwarepractice.entity.Accommendation;
import com.softwarepractice.entity.Information;

public interface UpdateInterface {
    Integer UpdateAccommendation(Accommendation accommendation);

    Integer UpdateInformation(Information information);

    Integer UpdateRepairStatus(Integer id,Integer status);

    Integer UpdateComplaintStatus(Integer id,Integer status);
}
