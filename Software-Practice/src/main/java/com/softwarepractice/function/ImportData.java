package com.softwarepractice.function;

import com.softwarepractice.dao.InsertInterface;
import com.softwarepractice.entity.Worker;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public void importWorkers() throws Exception {
        List<List<String>> results=analyzAny();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        InsertInterface insertInterface=sqlSession.getMapper(InsertInterface.class);
        for(int i=0;i<results.size();i++){
            List<String> workerParam=results.get(i);
            Worker worker=new Worker();
            worker.setName(workerParam.get(0));
            worker.setWorker_id(Integer.parseInt(workerParam.get(1)));
            worker.setTelephone(workerParam.get(2));
            worker.setType(workerParam.get(3));
            insertInterface.insertWorker(worker);
        }
        sqlSession.commit();
    }







    public List<List<String>> analyzAny() throws Exception {
        //Read Excel
        InputStream inputStream = new FileInputStream(file);
        List<List<String>> results = new ArrayList<>();

        if (type.equals(0)) {
            hssfWorkbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
                List<String> cells = new ArrayList<String>();
                Row row = sheet.getRow(i);
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    Cell cellinfo = row.getCell(j);
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
            for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
                List<String> cells = new ArrayList<String>();
                Row row = sheet.getRow(i);
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    Cell cellinfo = row.getCell(j);
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
