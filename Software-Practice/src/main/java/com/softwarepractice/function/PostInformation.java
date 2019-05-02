package com.softwarepractice.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.softwarepractice.dao.SelectInterface;
import com.softwarepractice.entity.Information;
import com.softwarepractice.entity.Push;
import com.softwarepractice.entity.Student;
import com.softwarepractice.message.wx.TemplateData;
import com.softwarepractice.message.wx.WxMssVo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostInformation {

    private SqlSessionFactory sqlSessionFactory;


    private String access_token;

    public PostInformation() {
        ApplicationContext applicationContext = new
                ClassPathXmlApplicationContext("spring-config.xml");
        sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
    }

    public void getAccess_token() throws Exception {
        //获取access_token
        URL url = new URL("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx72cd08754362b281&secret=bc97f9cd99ff0c623a33a6e1c7bd034a");

        // 2.获取HttpURRLConnection对象
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 3.调用connect方法连接远程资源
        connection.connect();
        // 4.访问资源数据，使用getInputStream方法获取一个输入流用以读取信息
        BufferedReader bReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "UTF-8"));

        // 对数据进行访问
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        // 关闭流
        bReader.close();
        // 关闭链接
        connection.disconnect();

        // 将获得的String对象转为JSON格式
        JSONObject jsonObject = JSONObject.parseObject(stringBuilder.toString());

        access_token = jsonObject.getString("access_token");
    }

    public void PostOneDorm(Integer dorm_id, Information information) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            URL url = new URL("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token);
            SelectInterface selectInterface = sqlSession.getMapper(SelectInterface.class);
            List<Student> students = selectInterface.FindStudentByDorm(dorm_id);
            if (students == null)
                return;
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                //拼接推送的模版
                WxMssVo wxMssVo = new WxMssVo();

                List<Push> pushs = selectInterface.FindPushBySid(student.getId());
                if ((pushs == null) || (student.getOpen_id() == null))
                    continue;

                Push push = pushs.get(0);
                if(push.getForm_id() == null)
                    continue;
                wxMssVo.setTouser(student.getOpen_id());//用户openid
                wxMssVo.setTemplate_id("NIVGuys6ewhHDqYBCbUcvfQGBo648Y7eebQdx6mmswg");//模版id
                wxMssVo.setForm_id(push.getForm_id());//formid

                Map<String, TemplateData> m = new HashMap<>();

                TemplateData keyword1 = new TemplateData();
                keyword1.setValue(information.getTitle());
                m.put("keyword1", keyword1);

                TemplateData keyword2 = new TemplateData();
                keyword2.setValue(information.getTime());
                m.put("keyword2", keyword2);
                wxMssVo.setData(m);


                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.connect();
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");

                out.append(JSON.toJSONString(wxMssVo));
                out.flush();
                out.close();

                // 关闭链接
                connection.disconnect();
            }
        } catch (Exception t) {

        } finally {
            sqlSession.close();
        }
    }

    public void PostAll(Information information) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            URL url = new URL("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token);

            SelectInterface selectInterface = sqlSession.getMapper(SelectInterface.class);
            List<Student> students = selectInterface.FindStudentAll();


            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                //拼接推送的模版
                WxMssVo wxMssVo = new WxMssVo();

                List<Push> pushs = selectInterface.FindPushBySid(student.getId());
                if ((pushs == null) || (student.getOpen_id() == null))
                    continue;

                Push push = pushs.get(0);
                if(push.getForm_id() == null)
                    continue;
                wxMssVo.setTouser(student.getOpen_id());//用户openid
                wxMssVo.setTemplate_id("NIVGuys6ewhHDqYBCbUcvfQGBo648Y7eebQdx6mmswg");//模版id
                wxMssVo.setForm_id(push.getForm_id());//formid

                Map<String, TemplateData> m = new HashMap<>();

                TemplateData keyword1 = new TemplateData();
                keyword1.setValue(information.getTitle());
                m.put("keyword1", keyword1);

                TemplateData keyword2 = new TemplateData();
                keyword2.setValue(information.getTime());
                m.put("keyword2", keyword2);
                wxMssVo.setData(m);


                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.connect();
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                out.append(JSON.toJSONString(wxMssVo));
                out.flush();
                out.close();
                // 关闭链接
                connection.disconnect();
            }
        } catch (Exception t) {

        } finally {
            sqlSession.close();
        }
    }


}
