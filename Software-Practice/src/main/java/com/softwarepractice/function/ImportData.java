package com.softwarepractice.function;

import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImportData {

    private SqlSessionFactory sqlSessionFactory;
    private File file;
    // 0:xls 1:xlsx
    private Integer type;
    //.xls
    private HSSFWorkbook hssfWorkbook = null;
    //.xlsx
    private XSSFWorkbook xssfWorkbook = null;

    public ImportData(File file, Integer type) {
        ApplicationContext applicationContext = new
                ClassPathXmlApplicationContext("spring-config.xml");
        sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        this.file = file;
        this.type = type;
    }

    public boolean importWorkers() throws Exception {
        List<List<String>> results = analyzAny();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        InsertInterface insertInterface = sqlSession.getMapper(InsertInterface.class);
        for (int i = 1; i < results.size(); i++) {
            List<String> workerParams = results.get(i);
            Worker worker = new Worker();
            worker.setName(workerParams.get(0));
            worker.setWorker_id(Integer.parseInt(workerParams.get(1)));
            worker.setTelephone(workerParams.get(2));
            worker.setType(workerParams.get(3));
            insertInterface.insertWorker(worker);
        }
        sqlSession.commit();
        return true;
    }

    public boolean imporFees() throws Exception {
        List<List<String>> results = analyzAny();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        InsertInterface insertInterface = sqlSession.getMapper(InsertInterface.class);
        SelectInterface selectInterface = sqlSession.getMapper(SelectInterface.class);
        for (int i = 1; i < results.size(); i++) {
            List<String> feeParams = results.get(i);
            Fee fee = new Fee();
            fee.setLast_month_quantity(Float.parseFloat(feeParams.get(0)));
            fee.setCurrent_month_quantity(Float.parseFloat(feeParams.get(1)));
            fee.setStart_time(feeParams.get(2));
            fee.setEnd_time(feeParams.get(3));
            fee.setUnit_price(Float.parseFloat(feeParams.get(4)));
            fee.setFree_quantity(Float.parseFloat(feeParams.get(5)));
            fee.setAmount(Float.parseFloat(feeParams.get(6)));
            fee.setStatus(0);
            fee.setType(Integer.parseInt(feeParams.get(7)));

            Dormitory dormitory = new Dormitory();
            dormitory.setZone(Integer.parseInt(feeParams.get(8)));
            dormitory.setBuilding(Integer.parseInt(feeParams.get(9)));
            dormitory.setRoom(Integer.parseInt(feeParams.get(10)));

            dormitory = selectInterface.selectDormitory(dormitory);
            if (dormitory != null) {
                fee.setD_id(dormitory.getId());
                insertInterface.insertFee(fee);
            } else {
                return false;
            }
        }
        sqlSession.commit();
        return true;
    }

    public boolean importAccommendation() throws Exception {
        List<List<String>> results = analyzAny();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        InsertInterface insertInterface = sqlSession.getMapper(InsertInterface.class);
        SelectInterface selectInterface = sqlSession.getMapper(SelectInterface.class);
        for (int i = 1; i < results.size(); i++) {
            List<String> stuParams = results.get(i);
            Student student = new Student();
            student.setName(stuParams.get(0));
            student.setStudent_id(Integer.parseInt(stuParams.get(1)));
            student.setTelephone(stuParams.get(2));
            student.setIs_leader(Integer.parseInt(stuParams.get(3)));

            Dormitory dormitory = new Dormitory();
            dormitory.setZone(Integer.parseInt(stuParams.get(4)));
            dormitory.setBuilding(Integer.parseInt(stuParams.get(5)));
            dormitory.setRoom(Integer.parseInt(stuParams.get(6)));
            dormitory = selectInterface.selectDormitory(dormitory);
            if (dormitory != null) {
                Student stuExist = selectInterface.selectStudentByStudentId(student);
                Accommendation accommendation = new Accommendation();
                if (stuExist != null) {
                    accommendation.setS_id(stuExist.getId());
                    accommendation.setD_id(dormitory.getId());
                } else {
                    insertInterface.insertStudent(student);
                    accommendation.setS_id(student.getId());
                    accommendation.setD_id(dormitory.getId());
                }
                Accommendation accomExist = selectInterface.selectAccommendation(accommendation);
                if (accomExist != null) {
                    continue;
                } else {
                    insertInterface.insertAccommendation(accommendation);
                }
            } else {
                return false;
            }
        }
        sqlSession.commit();
        return true;
    }


    public List<List<String>> analyzAny() throws Exception {
        //Read Excel
        InputStream inputStream = new FileInputStream(file);
        List<List<String>> results = new ArrayList<>();

        if (type.equals(0)) {
            hssfWorkbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                List<String> cells = new ArrayList<String>();
                Row row = sheet.getRow(i);
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    Cell cellinfo = row.getCell(j);
                    cellinfo.setCellType(CellType.STRING);
                    if (cellinfo == null) {
                        cells.add("");
                    } else {
                        cells.add(cellinfo.getStringCellValue());
                    }
                }
                results.add(cells);
            }
        } else {
            xssfWorkbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                List<String> cells = new ArrayList<String>();
                Row row = sheet.getRow(i);
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    Cell cellinfo = row.getCell(j);
                    cellinfo.setCellType(CellType.STRING);
                    if (cellinfo == null) {
                        cells.add("");
                    } else {
                        cells.add(cellinfo.getStringCellValue());
                    }
                }
                results.add(cells);
            }
        }
        return results;
    }


}
