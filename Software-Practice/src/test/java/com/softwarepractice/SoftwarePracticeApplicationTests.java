package com.softwarepractice;

import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Admin;
import com.softwarepractice.function.Token;
import com.softwarepractice.message.error.ErrorMessage;
import com.softwarepractice.message.login.LoginSuccess;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SoftwarePracticeApplicationTests {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @Test
    public void contextLoads() {
    }

    @Test
    public void mybatistest(){
        try {
            SqlSession session = sqlSessionFactoryBean.getObject().openSession();
            SelectInterface selectInterface = session.getMapper(SelectInterface.class);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
