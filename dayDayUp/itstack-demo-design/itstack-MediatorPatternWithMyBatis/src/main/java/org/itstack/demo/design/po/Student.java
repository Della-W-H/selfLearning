package org.itstack.demo.design.po;

import java.util.Date;

/**
 * @author wanghong
 * @date 2022/7/8
 * @apiNote
 */
public class Student {
    private String id;
    private String name;
    private String birth;
    private String sex;

    public String getS_id() {
        return id;
    }

    public void setS_id(String id) {
        this.id = id;
    }

    public String getS_name() {
        return name;
    }

    public void setS_name(String name) {
        this.name = name;
    }

    public String getS_birth() {
        return birth;
    }

    public void setS_birth(String birth) {
        this.birth = birth;
    }

    public String getS_sex() {
        return sex;
    }

    public void setS_sex(String sex) {
        this.sex = sex;
    }
}
