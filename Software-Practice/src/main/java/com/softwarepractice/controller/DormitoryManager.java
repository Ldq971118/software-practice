package com.softwarepractice.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Accommendation;
import com.softwarepractice.entity.Dormitory;
import com.softwarepractice.entity.Student;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import com.softwarepractice.message.success.SuccessMessage;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/web/dormitory")
public class DormitoryManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/saveDormitory", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface AddDormitory(@RequestBody Map<String, Object> datamap, HttpServletRequest request)
            throws Exception {
        String token = request.getHeader("token");
        Integer jurisdirction = Token.GetJurisdirction(token);

        ErrorMessage fail = new ErrorMessage("权限不足");
        SuccessMessage success = new SuccessMessage();

        if (jurisdirction != 0)
            return fail;
        else {
            //init Dormitory
            Dormitory dormitory = new Dormitory();
            dormitory.setBuilding((Integer) datamap.get("building"));
            dormitory.setRoom((Integer) datamap.get("room"));
            dormitory.setZone((Integer) datamap.get("zone"));

            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            InsertInterface insertInterface = session.getMapper(InsertInterface.class);
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);

            Dormitory selectDormitory = selectInterface.SelectDormitory(dormitory);
            if(selectDormitory==null){
                fail.setErrMsg("宿舍不存在");
                return fail;
            }

            //插入学生
            Student student=new Student();
            student.setIs_leader((Integer) datamap.get("is_leader"));
            student.setName((String)datamap.get("name"));
            student.setStudent_id(Integer.parseInt((String)datamap.get("studentId")));
            student.setTelephone(Integer.parseInt((String)datamap.get("tel")));

            Integer effect=insertInterface.InsertStudent(student);
            if (effect != 1) {
                fail.setErrMsg("添加失败");
                return fail;
            }

            Accommendation accommendation=new Accommendation();
            accommendation.setD_id(selectDormitory.getId());
            accommendation.setS_id(student.getId());

            effect=insertInterface.InsertAccommendation(accommendation);
            if (effect != 1) {
                fail.setErrMsg("添加失败");
                return fail;
            }
            session.commit();
            session.close();
            return success;
        }
    }

//    @RequestMapping(value = "/getAllDormitories", method = RequestMethod.GET)
//    @ResponseBody
//    public PageInfo<Dormitory> GetAllDormitories(Integer pageNum, Integer pageSize, HttpServletRequest request)
//            throws Exception {
//        if (pageNum < 0 || pageSize <= 0)
//            throw new Exception("Num Error");
//        else {
//            String token = request.getHeader("header");
//            if (!Token.varify(token))
//                throw new Exception("Token Error");
//            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
//            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
//            PageHelper.startPage(pageNum, pageSize);
//            List<Dormitory> dormitoryList = selectInterface.FindDormitoryAll();
//            PageInfo<Dormitory> dormitoryPageInfo = new PageInfo<>(dormitoryList);
//            session.close();
//            return dormitoryPageInfo;
//        }
//
//    }


}
