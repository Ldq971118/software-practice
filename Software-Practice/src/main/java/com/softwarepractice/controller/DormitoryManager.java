package com.softwarepractice.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.DeleteInterface;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.dao.UpdateInterface;
import com.softwarepractice.entity.Accommendation;
import com.softwarepractice.entity.Dormitory;
import com.softwarepractice.entity.Student;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import com.softwarepractice.message.medium.AccommendationMessage;
import com.softwarepractice.message.medium.Response;
import com.softwarepractice.message.success.SuccessMessage;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/web/dormitory")
@CrossOrigin
public class DormitoryManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/saveDormitory", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface addDormitory(@RequestBody Map<String, Object> datamap, HttpServletRequest request)
            throws Exception {
        String token = request.getHeader("token");
        Integer jurisdirction = Token.getJurisdirction(token);

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

            Dormitory selectDormitory = selectInterface.selectDormitory(dormitory);
            if (selectDormitory == null) {
                fail.setErrMsg("宿舍不存在");
                return fail;
            }

            //插入学生
            Student student = new Student();
            student.setIs_leader((Integer) datamap.get("is_leader"));
            student.setName((String) datamap.get("name"));
            student.setStudent_id(Integer.parseInt((String) datamap.get("studentId")));
            student.setTelephone((String) datamap.get("tel"));

            Student exist_stu = selectInterface.selectStudentByStudentId(student);
            Integer effect;
            if (exist_stu == null) {
                effect = insertInterface.insertStudent(student);
                if (effect != 1) {
                    fail.setErrMsg("添加失败");
                    return fail;
                }
            } else
                student.setId(exist_stu.getId());

            Accommendation accommendation = new Accommendation();
            accommendation.setD_id(selectDormitory.getId());
            accommendation.setS_id(student.getId());

            effect = insertInterface.insertAccommendation(accommendation);
            if (effect != 1) {
                fail.setErrMsg("添加失败");
                return fail;
            }
            session.commit();
            session.close();
            return success;
        }
    }

    @RequestMapping(value = "/getAllDormitories", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getAllDormitories(Integer pageNum, Integer pageSize, HttpServletRequest request)
            throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("token");
            if (!Token.varify(token))
                throw new Exception("Token Error");
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<AccommendationMessage> accommendationAll =
                    selectInterface.findAccommendationAll(Token.getJurisdirction(token));
            PageInfo<AccommendationMessage> accommendationMessagePageInfo =
                    new PageInfo<>(accommendationAll);
            Response response=new Response(accommendationMessagePageInfo);
            session.close();
            return response;
        }

    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface updateAccommendation(@RequestBody Map<String, Object> datamap, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Integer jurisdirction = Token.getJurisdirction(token);

        ErrorMessage fail = new ErrorMessage("权限不足");
        SuccessMessage success = new SuccessMessage();

        if (jurisdirction != 0)
            return fail;
        else {
            Integer id= (Integer) datamap.get("id");
            Dormitory dormitory=new Dormitory();
            dormitory.setZone((Integer) datamap.get("zone"));
            dormitory.setRoom((Integer) datamap.get("room"));
            dormitory.setBuilding((Integer) datamap.get("building"));
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            dormitory=selectInterface.selectDormitory(dormitory);
            if(dormitory==null){
                fail.setErrMsg("宿舍信息错误");
                return fail;
            }else{
                Accommendation accommendation=new Accommendation();
                accommendation.setD_id(dormitory.getId());
                accommendation.setId(id);
                UpdateInterface updateInterface=session.getMapper(UpdateInterface.class);
                Integer effect=updateInterface.updateAccommendation(accommendation);
                if(effect!=1){
                    fail.setErrMsg("住宿信息错误");
                    return fail;
                }
            }
            session.commit();
            session.close();
            return success;
        }
    }

    @RequestMapping(value = "/removeById", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface deleteAccommendation(Integer id,HttpServletRequest request) throws Exception{
        String token = request.getHeader("token");
        Integer jurisdirction = Token.getJurisdirction(token);

        ErrorMessage fail = new ErrorMessage("权限不足");
        SuccessMessage success = new SuccessMessage();

        if (jurisdirction != 0)
            return fail;
        else {
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            DeleteInterface deleteInterface = session.getMapper(DeleteInterface.class);
            Integer effect=deleteInterface.deleteAccommendationById(id);
            if(effect!=1){
                fail.setErrMsg("住宿信息不存在");
                return fail;
            }
            session.commit();
            session.close();
            return success;
        }
    }
}
