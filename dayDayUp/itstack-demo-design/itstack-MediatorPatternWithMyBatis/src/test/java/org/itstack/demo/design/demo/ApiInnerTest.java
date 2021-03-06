package org.itstack.demo.design.demo;

import com.alibaba.fastjson.JSON;
import org.itstack.demo.design.mediator.Resources;
import org.itstack.demo.design.mediator.SqlSession;
import org.itstack.demo.design.mediator.SqlSessionFactory;
import org.itstack.demo.design.mediator.SqlSessionFactoryBuilder;
import org.itstack.demo.design.po.Student;
import org.itstack.demo.design.po.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.List;

public class ApiInnerTest {

    private Logger logger = LoggerFactory.getLogger(ApiInnerTest.class);

    @Test
    public void test_queryStudentInfoById() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

            SqlSession session = sqlMapper.openSession();
            try {
                Student student = session.selectOne("org.itstack.demo.design.dao.IStudentDao.queryStudentInfoById", 4);
                logger.info("测试结果：{}", JSON.toJSONString(student));
            } finally {
                session.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test_queryUserList() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

            SqlSession session = sqlMapper.openSession();
            try {
                User req = new User();
                req.setAge(18);
                List<User> userList = session.selectList("org.itstack.demo.design.dao.IUserDao.queryUserList", req);
                logger.info("测试结果：{}", JSON.toJSONString(userList));
            } finally {
                session.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
