package com.softwarepractice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.DeleteInterface;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.dao.UpdateInterface;
import com.softwarepractice.entity.Accommendation;
import com.softwarepractice.entity.Dormitory;
import com.softwarepractice.entity.Student;
import com.softwarepractice.function.ImportData;
import com.softwarepractice.function.Token;

import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.Success;
import com.softwarepractice.message.medium.AccommendationMessage;
import com.softwarepractice.message.Response;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequestMapping("/api/web/dormitory")
@CrossOrigin
public class DormitoryManager {

    private static final String path = "/tmp";
    private static final String[] format = new String[]{"xls", "xlsx"};

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/saveDormitory", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface addDormitory(@RequestBody Map<String, Object> datamap, HttpServletRequest request)
            throws Exception {
        String token = request.getHeader("token");
        Integer jurisdirction = Token.getJurisdirction(token);

        Success success = new Success();

        if (jurisdirction != 0) {
            throw new Exception("Permission Denied");
        } else {
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
                throw new Exception("No Exist");
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
                    throw new Exception("Add Fail");
                }
            } else {
                student.setId(exist_stu.getId());
            }

            Accommendation accommendation = new Accommendation();
            accommendation.setD_id(selectDormitory.getId());
            accommendation.setS_id(student.getId());

            effect = insertInterface.insertAccommendation(accommendation);
            if (effect != 1) {
                throw new Exception("Add Fail");
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
        if (pageNum < 0 || pageSize <= 0) {
            throw new Exception("Num Error");
        } else {
            String token = request.getHeader("token");
            if (!Token.varify(token)) {
                throw new Exception("Token Error");
            }
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<AccommendationMessage> accommendationAll =
                    selectInterface.findAccommendationAll(Token.getJurisdirction(token));
            PageInfo<AccommendationMessage> accommendationMessagePageInfo =
                    new PageInfo<>(accommendationAll);
            Response response = new Response(accommendationMessagePageInfo);
            session.close();
            return response;
        }

    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface updateAccommendation(@RequestBody Map<String, Object> datamap, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Integer jurisdirction = Token.getJurisdirction(token);

        Success success = new Success();

        if (jurisdirction != 0) {
            throw new Exception("Permission Denied");
        } else {
            Integer id = (Integer) datamap.get("id");
            Dormitory dormitory = new Dormitory();
            dormitory.setZone((Integer) datamap.get("zone"));
            dormitory.setRoom((Integer) datamap.get("room"));
            dormitory.setBuilding((Integer) datamap.get("building"));
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            dormitory = selectInterface.selectDormitory(dormitory);
            if (dormitory == null) {
                throw new Exception("No Exist");
            } else {
                Accommendation accommendation = new Accommendation();
                accommendation.setD_id(dormitory.getId());
                accommendation.setId(id);
                UpdateInterface updateInterface = session.getMapper(UpdateInterface.class);
                Integer effect = updateInterface.updateAccommendation(accommendation);
                if (effect != 1) {
                    throw new Exception("Change Fail");
                }
            }
            session.commit();
            session.close();
            return success;
        }
    }

    @RequestMapping(value = "/removeById", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface deleteAccommendation(Integer id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Integer jurisdirction = Token.getJurisdirction(token);
        Success success=new Success();

        if (jurisdirction != 0) {
            throw new Exception("Permission Denied");
        } else {
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            DeleteInterface deleteInterface = session.getMapper(DeleteInterface.class);
            Integer effect = deleteInterface.deleteAccommendationById(id);
            if (effect != 1) {
                throw new Exception("No Exist");
            }
            session.commit();
            session.close();
            return success;
        }
    }

    @RequestMapping(value = "/uploadDorms", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface uploadDorms(@RequestParam("file") MultipartFile mulFile, HttpServletRequest request) throws Exception {

        String token = request.getHeader("token");
        if (!Token.varify(token)) {
            throw new Exception("Token Error");
        }
        if (Token.getJurisdirction(token) != 0) {
            throw new Exception("Permission Denied");
        }

        //获取原始文件名称
        String originalFileName = mulFile.getOriginalFilename();

        //获取文件类型，以最后一个`.`作为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();


        if (!type.equals(format[0]) && !type.equals(format[1])) {
            throw new Exception("Upload Fail");
        }

        //设置文件新名字
        String fileName = System.currentTimeMillis() + "dorm" + "." + type;

        //在指定路径创建一个文件
        File targetFile = new File(path, fileName);

        //将文件保存到服务器指定位置
        try {
            mulFile.transferTo(targetFile);
        } catch (IOException e) {
            throw new Exception("Upload Fail");
        }

        ImportData importData;
        if (type.equals(format[0])) {
            importData = new ImportData(targetFile, 0);
        } else {
            importData = new ImportData(targetFile, 1);
        }

        boolean result = importData.importAccommendation();
        targetFile.delete();
        if (result) {
            Success successMessage = new Success();
            return successMessage;
        } else {
            throw new Exception("Import Data Fail");
        }
    }
}
