package com.softwarepractice.dao;

import com.softwarepractice.entity.*;
import com.softwarepractice.message.medium.*;
import com.softwarepractice.message.information.InformationsMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SelectInterface {
    Admin selectAdminByWorkerId(Integer worker_id);

    List<Worker> findWorkerAll();

    List<feeMessage> findFeeAll(@Param("jurisdirction") Integer jurisdirction);

    List<feeMessage> findFeeByType(@Param("jurisdirction") Integer jurisdirction, Integer type);

    List<RepairMessage> findRepairAll(@Param("jurisdirction") Integer jurisdirction);

    List<ComplaintMessage> findComplaintAll(@Param("jurisdirction") Integer jurisdirction);

    List<Questionnaire> findQuestionnaireAll(@Param("jurisdirction") Integer jurisdirction);

    //获取问卷详细
    Questionnaire selectQuestionnaireById(Integer id);

    List<Question> findQuestionAllByQuestionaireId(Integer questionnaire_id);

    List<Options> findOptionsAllByQuestionId(Integer question_id);

    Dormitory selectDormitoryById(Dormitory dormitory);

    Dormitory selectDormitory(Dormitory dormitory);

    Student selectStudentByStudentId(Student student);

    List<AccommendationMessage> findAccommendationAll(@Param("zone") Integer zone);

    List<InformationsMessage> findInformationAll();

    Worker selectWorkerByWorkerId(Integer worker_id);

    Worker selectWorkerById(Integer id);

    Information selectInformationById(Integer id);

    List<Student> findStudentAll();

    List<Student> findStudentBySome(@Param("zone") Integer zone,
                                    @Param("building") Integer building,
                                    @Param("room") Integer room);

    List<Push> findPushBySid(Integer s_id);

    Accommendation selectAccommendation(Accommendation accommendation);

    Complaint selectComplaintByIdAndZone(@Param("id") Integer id,
                                         @Param("jurisdirction") Integer jurisdirction);

    Repair selectRepairByIdAndZone(@Param("id") Integer id,
                                         @Param("jurisdirction") Integer jurisdirction);

    List<Reply> selectRepairReplyById(Integer id);

    List<Reply> selectComplaintReplyById(Integer id);
//    List<Dormitory> FindDormitoryAll();
}
