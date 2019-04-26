package com.softwarepractice.dao;

import com.softwarepractice.entity.*;
import com.softwarepractice.message.medium.AccommendationMessage;
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

    //获取问卷详细
    Questionnaire SelectQuestionnaire(Integer id);
    List<Question> FindQuestionAll(Integer questionnarie_id);
    List<Options> FindOptionsAll(Integer question_id);

    Dormitory SelectDormitory(Dormitory dormitory);
    Student SelectStudent(Student student);
    List<AccommendationMessage> FindAccommendationAll(Integer zone);

//    List<Student> FindStudentAll();
//    List<Dormitory> FindDormitoryAll();
}
