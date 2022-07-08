package org.itstack.demo.design.dao;

import org.itstack.demo.design.po.Student;

/**
 * @author wanghong
 * @date 2022/7/8
 * @apiNote
 */
public interface IStudentDao {
    Student queryStudentInfoById(int id);
}
