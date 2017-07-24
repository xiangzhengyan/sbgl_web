package com.zfysoft.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 字典表
 * @author Administrator
 *
 */
@Entity
@Table(name="p_zd_item")
public class ZdItem {
	/*主键*/
	@Id
	@GeneratedValue(generator = "all_native")
	@GenericGenerator(strategy = "native", name = "all_native")
	@Column(name = "id", unique = true, nullable = false, precision = 38, scale = 0)
	private Long id;
	/*名称*/
	@Column(name="label")
	private String label;
	/*code*/
	@Column(name="code")
	private String code;
	/*图片*/
	@Column(name="img")
	private String img;
	/*是否允许删除 1允许0不允许*/
	@Column(name="allowdel")
	private String allowdel = "1";
	/*预留字段*/
	@Column(name="reserve_field")
	private String reserveField;
	/*字段分组code*/
	@Column(name="zd_group_code")
	private String zdGroupCode;
	/*从属code*/
	@Column(name="parent_code")
	private String parentCode;
	/*排序*/
	@Column(name="item_index")
	private String index;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getAllowdel() {
		return allowdel;
	}
	public void setAllowdel(String allowdel) {
		this.allowdel = allowdel;
	}
	public String getReserveField() {
		return reserveField;
	}
	public void setReserveField(String reserveField) {
		this.reserveField = reserveField;
	}
	public String getZdGroupCode() {
		return zdGroupCode;
	}
	public void setZdGroupCode(String zdGroupCode) {
		this.zdGroupCode = zdGroupCode;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
}
