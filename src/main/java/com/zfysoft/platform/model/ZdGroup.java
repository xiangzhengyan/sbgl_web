package com.zfysoft.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 字典分组表
 * @author Administrator
 *
 */
@Entity
@Table(name="p_zd_group")
public class ZdGroup {
	/*主键*/
	@Id
	@GeneratedValue(generator = "all_native")
	@GenericGenerator(strategy = "native", name = "all_native")
	@Column(name = "id", unique = true, nullable = false, precision = 38, scale = 0)
	private Long id;
	/*组名称*/
	@Column(name="group_label")
	private String groupLabel;
	/*组code*/
	@Column(name="group_code")
	private String groupCode;
	/*从属组*/
	@Column(name="parent_code")
	private String parentCode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGroupLabel() {
		return groupLabel;
	}
	public void setGroupLabel(String groupLabel) {
		this.groupLabel = groupLabel;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}
