package com.softwarepractice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.DeleteInterface;
import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Worker;
import com.softwarepractice.function.ImportData;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.Success;
import com.softwarepractice.message.medium.Response;
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
@RequestMapping("/api/web/worker")
@CrossOrigin
public class WorkerManager {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    private static final String path = "/tmp";
    private static final String[] format = new String[]{"xls", "xlsx"};

    @RequestMapping(value = "/getAllWorkers", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getAllWorkers(Integer pageNum, Integer pageSize, HttpServletRequest request) throws Exception {
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
            List<Worker> workerList = selectInterface.findWorkerAll();
            PageInfo<Worker> workerPageInfo = new PageInfo<>(workerList);
            Response response = new Response(workerPageInfo);
            session.close();
            return response;
        }
    }

    @RequestMapping(value = "/saveWorker", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface addWorkers(@RequestBody Map<String, Object> worker_map, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Integer jurisdirction = Token.getJurisdirction(token);

        Success success = new Success();

        if (jurisdirction != 0) {
            throw new Exception("Permission Denied");
        } else {
            //init worker
            Worker worker = new Worker();
            worker.setName((String) worker_map.get("name"));
            worker.setWorker_id((Integer) worker_map.get("workerId"));
            worker.setTelephone((String) worker_map.get("tel"));
            worker.setType((String) worker_map.get("type"));

            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            InsertInterface insertInterface = session.getMapper(InsertInterface.class);
            Integer effect = insertInterface.insertWorker(worker);
            session.commit();
            session.close();
            if (effect != 1) {
                throw new Exception("Add Fail");
            } else {
                return success;
            }
        }
    }

    @RequestMapping(value = "/removeById", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface deleteWorker(Integer id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Integer jurisdirction = Token.getJurisdirction(token);

        Success success = new Success();

        if (jurisdirction != 0) {
            throw new Exception("Permission Denied");
        }
        else {
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            DeleteInterface deleteInterface = session.getMapper(DeleteInterface.class);
            Integer effect = deleteInterface.deleteWorkerById(id);
            session.commit();
            session.close();

            if (effect != 1) {
                throw new Exception("Delete Fail");
            } else {
                return success;
            }
        }
    }

    @RequestMapping(value = "/uploadWorkers", method = RequestMethod.POST)
    @ResponseBody
    public MessageInterface uploadWorkers(@RequestParam("file") MultipartFile mulFile, HttpServletRequest request) throws Exception {

        String token = request.getHeader("token");
        if (!Token.varify(token)) {
            throw new Exception("Token Error");
        }
        Integer jurisdirction = Token.getJurisdirction(token);
        if (jurisdirction != 0) {
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
        String fileName = System.currentTimeMillis() + "worker" + "." + type;

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

        boolean result = importData.importWorkers();
        targetFile.delete();
        if (result) {
            Success successMessage = new Success();
            return successMessage;
        } else {
            throw new Exception("Import Data Fail");
        }
    }
}
