package com.heima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.pojo.Resource;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ResourceMapper extends BaseMapper<Resource> {


    @Select("select * from tb_resource where id in (select resource_id from tb_role_resource where role_id in (select role_id from tb_admin_role where admin_id = (select id from tb_admin where login_name = #{loginName})));")
    List<Resource> findResouceByLoginName(String loginName);

}
