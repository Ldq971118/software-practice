package com.softwarepractice.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.MessageInterface;
import com.softwarepractice.message.error.ErrorMessage;
import com.softwarepractice.message.fee.feeMessage;
import com.softwarepractice.message.medium.Response;
import com.softwarepractice.message.success.SuccessMessage;
import com.sun.org.apache.bcel.internal.util.ClassPath;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/web/fee")
@CrossOrigin
public class FeeManager {

    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 10;  // 3MB
    private static final int maxMemSize = 5000 * 1024;

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @RequestMapping(value = "/getAllFees", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getAllFees(Integer pageNum, Integer pageSize, Integer type, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("token");
            if (!Token.varify(token))
                throw new Exception("Token Error");
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<feeMessage> feeList = selectInterface.findFeeAll(Token.getJurisdirction(token));
            PageInfo<feeMessage> feePageInfo = new PageInfo<>(feeList);
            Response response = new Response(feePageInfo);
            session.close();
            return response;
        }
    }

    @RequestMapping(value = "/getWaterFees", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getWaterFees(Integer pageNum, Integer pageSize, Integer type, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("token");
            if (!Token.varify(token))
                throw new Exception("Token Error");
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<feeMessage> feeList = selectInterface.findFeeByType(Token.getJurisdirction(token), type);
            PageInfo<feeMessage> feePageInfo = new PageInfo<>(feeList);
            Response response = new Response(feePageInfo);
            session.close();
            return response;
        }
    }

    @RequestMapping(value = "/getElectricityFees", method = RequestMethod.GET)
    @ResponseBody
    public MessageInterface getElectricityFees(Integer pageNum, Integer pageSize, Integer type, HttpServletRequest request) throws Exception {
        if (pageNum < 0 || pageSize <= 0)
            throw new Exception("Num Error");
        else {
            String token = request.getHeader("token");
            if (!Token.varify(token))
                throw new Exception("Token Error");
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
            PageHelper.startPage(pageNum, pageSize);
            List<feeMessage> feeList = selectInterface.findFeeByType(Token.getJurisdirction(token), type);
            PageInfo<feeMessage> feePageInfo = new PageInfo<>(feeList);
            Response response = new Response(feePageInfo);
            session.close();
            return response;
        }
    }

//    @RequestMapping(value = "/uploadFees", method = RequestMethod.POST)
//    @ResponseBody
//    public MessageInterface uploadFees(@RequestParam MultipartFile mulFile,HttpServletRequest request) throws Exception {
//
//        SuccessMessage successMessage=new SuccessMessage();
//
//        //获取原始文件名称
//        String originalFileName = mulFile.getOriginalFilename();
//        System.out.println("原始文件名称：" + originalFileName);
//
//        //获取文件类型，以最后一个`.`作为标识
//        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
//        System.out.println("文件类型：" + type);
//
//        //设置文件新名字
//        String fileName = System.currentTimeMillis() + "." + type;
//        System.out.println("文件新名称：" + fileName);
//        //在指定路径创建一个文件
//        File targetFile = new File(fileName);
//
//        //将文件保存到服务器指定位置
//        try {
//            mulFile.transferTo(targetFile);
//        } catch (IOException e) {
//            ErrorMessage errorMessage=new ErrorMessage("上传失败");
//            return errorMessage;
//        }
//        return successMessage;
//    }
}
