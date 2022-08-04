package com.della.pojo;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * resource实体类
 * @author Administrator
 *
 */
@TableName("tb_Resource")
public class Resource implements Serializable{

	private Integer id;//id


	

	private String resKey;//res_key

	private String resName;//res_name

	private Integer parentId;//parent_id

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getResKey() {
		return resKey;
	}
	public void setResKey(String resKey) {
		this.resKey = resKey;
	}

	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}

	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}


	
}
