package com.softwarepractice.dao;

import com.softwarepractice.entity.*;
import com.softwarepractice.message.fee.feeMessage;
import com.softwarepractice.message.medium.AccommendationMessage;
import com.softwarepractice.message.medium.InformationsMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SelectInterface {
    Admin SelectAdmin(Integer worker_id);
    List<Worker> FindWorkerAll();
    List<feeMessage> FindFeeAll(@Param("jurisdirction") Integer jurisdirction);
    List<feeMessage> FindFeeByType(@Param("jurisdirction")Integer jurisdirction,Integer type);
    List<Repair> FindRepairAll(@Param("jurisdirction")Integer jurisdirction);
    List<Complaint> FindComplaintAll(@Param("jurisdirction")Integer jurisdirction);
    List<Questionnaire> FindQuestionnaireAll(@Param("jurisdirction")Integer jurisdirction);

    //获取问卷详细
    Questionnaire SelectQuestionnaire(Integer id);
    List<Question> FindQuestionAll(Integer questionnaire_id);
    List<Options> FindOptionsAll(Integer question_id);

    Dormitory SelectDormitoryById(Dormitory dormitory);
    Dormitory SelectDormitory(Dormitory dormitory);
    Student SelectStudent(Student student);
    List<AccommendationMessage> FindAccommendationAll(@Param("zone") Integer zone);

    List<InformationsMessage> FindInformationAll();
    Worker SelectWorker(Integer worker_id);
    Worker SelectWorkerById(Integer id);
    Information SelectInformation(Integer id);

    List<Student> FindStudentAll();
    List<Student> FindStudentBySome(@Param("zone") Integer zone,
                                    @Param("building") Integer building,
                                    @Param("room") Integer room);
    List<Push> FindPushBySid(Integer s_id);
//    List<Dormitory> FindDormitoryAll();
}
