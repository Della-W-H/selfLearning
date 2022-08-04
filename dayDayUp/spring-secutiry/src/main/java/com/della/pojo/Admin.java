package com.della.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tb_admin")
@Data
public class Admin {
    private int id;
    @TableField("login_name")
    private String loginName;

    private String password;

    private int status;
}
