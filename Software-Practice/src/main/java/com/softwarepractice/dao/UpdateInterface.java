package com.softwarepractice.dao;

import com.softwarepractice.entity.Accommendation;
import com.softwarepractice.entity.Information;

public interface UpdateInterface {
    Integer updateAccommendation(Accommendation accommendation);

    Integer updateInformation(Information information);

    Integer updateRepairStatus(Integer id,Integer status);

    Integer updateComplaintStatus(Integer id,Integer status);

    Integer updateRepairNewTime(Integer id);

    Integer updateComplaintNewTime(Integer id);
}
