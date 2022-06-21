package com.hong.PowerMockito;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * @author wanghong
 * @date 2022/5/9
 * @apiNote
 */
public class HumanService {

    @Autowired
    private StudentDao studentDao;

    public void getOneStudent() {
        Student one = studentDao.getOne();
        ArrayList<Student> students = new ArrayList<>();

        if (null == one) {
            System.out.println("没有学生");
        } else {
            students.add(one);
            System.out.println(one.getName() + "~" + students.get(0));
        }
    }

    public void insertOne(Student student) {
        studentDao.insert(student);
    }

    public void update(Student student) {
        studentDao.update(student);
    }
}
