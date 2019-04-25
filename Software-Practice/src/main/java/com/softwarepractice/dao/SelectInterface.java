package com.softwarepractice.dao;

import com.softwarepractice.entity.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SelectInterface {
    Admin SelectAdmin(Integer w_id);
    List<Worker> FindWorkerAll();
    List<Fee> FindFeeAll(Integer jurisdirction);
    List<Repair> FindRepairAll(Integer jurisdirction);
    List<Complaint> FindComplaintAll(Integer jurisdirction);
    List<Questionnaire> FindQuestionnaireAll(Integer jurisdirction);
    List<Student> FindStudentAll();
    List<Dormitory> FindDormitoryAll();
}
