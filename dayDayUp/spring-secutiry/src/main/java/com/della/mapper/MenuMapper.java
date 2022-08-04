package com.della.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.della.pojo.Menu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {


    @Select("select * from tb_menu where id in (select menu_id from tb_resource_menu where resource_id in (select resource_id from tb_role_resource where role_id in (select role_id from tb_admin_role where admin_id = (select id from tb_admin where login_name = #{loginName}))))\n" +
            "UNION\n" +
            "select * from tb_menu where id in(select parent_id from tb_menu where id in (select menu_id from tb_resource_menu where resource_id in (select resource_id from tb_role_resource where role_id in (select role_id from tb_admin_role where admin_id = (select id from tb_admin where login_name = #{loginName})))))\n" +
            "UNION\n" +
            "select * from tb_menu where id in(\n" +
            "select parent_id from tb_menu where id in(select parent_id from tb_menu where id in (select menu_id from tb_resource_menu where resource_id in (select resource_id from tb_role_resource where role_id in (select role_id from tb_admin_role where admin_id = (select id from tb_admin where login_name = #{loginName}))))))")
    List<Menu> findMenusByLoginName(String loginName);
}
