package com.softwarepractice.dao;

import com.softwarepractice.entity.*;
import com.softwarepractice.message.fee.feeMessage;
import com.softwarepractice.message.medium.AccommendationMessage;
import com.softwarepractice.message.medium.InformationsMessage;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SelectInterface {
    Admin SelectAdmin(Integer w_id);
    List<Worker> FindWorkerAll();
    List<feeMessage> FindFeeAll(Integer jurisdirction);
    List<feeMessage> FindFeeByType(Integer jurisdirction,Integer type);
    List<Repair> FindRepairAll(Integer jurisdirction);
    List<Complaint> FindComplaintAll(Integer jurisdirction);
    List<Questionnaire> FindQuestionnaireAll(Integer jurisdirction);

    //获取问卷详细
    Questionnaire SelectQuestionnaire(Integer id);
    List<Question> FindQuestionAll(Integer questionnarie_id);
    List<Options> FindOptionsAll(Integer question_id);

    Dormitory SelectDormitoryById(Dormitory dormitory);
    Dormitory SelectDormitory(Dormitory dormitory);
    Student SelectStudent(Student student);
    List<AccommendationMessage> FindAccommendationAll(Integer zone);

    List<Information> FindInformationAll();
    Worker SelectWorker(Integer w_id);
    Information SelectInformation(Integer id);

    List<Student> FindStudentByDorm(Integer dorm_id);
    List<Student> FindStudentAll();
    List<Push> FindPushBySid(Integer s_id);
//    List<Dormitory> FindDormitoryAll();
}
