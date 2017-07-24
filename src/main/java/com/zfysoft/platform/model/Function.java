package com.zfysoft.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
//@SequenceGenerator(name="p_sequence", sequenceName="p_sequence")
@Table(name="p_function")
public class Function implements Serializable{
	
	private static final long serialVersionUID = 6991103418546729884L;
	private Long id;
	private Function parent;
	private String label;
	private String url;
	private String icoName;
	private Function app; // 子系统ID
	private Long menuIndex; // 排序号
	private Long menuLevel; // 层级号
	private Long enable;
	
	@Id
	@GeneratedValue(generator = "all_native")
	@GenericGenerator(strategy = "native", name = "all_native")
	//@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="p_sequence")
	@Column(name = "id", unique = true, nullable = false, precision = 12, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(optional=true,fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id", nullable = true)
	public Function getParent() {
		return parent;
	}
	public void setParent(Function parent) {
		this.parent = parent;
	}
	
	@Column(name="label")
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Column(name="url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name="ico_name")
	public String getIcoName() {
		return icoName;
	}
	public void setIcoName(String icoName) {
		this.icoName = icoName;
	}
	@ManyToOne(optional=true,fetch = FetchType.EAGER)
	@JoinColumn(name = "app_id", nullable = true)
	public Function getApp() {
		return app;
	}
	public void setApp(Function app) {
		this.app = app;
	}
	
	@Column(name="menu_index")
	public Long getMenuIndex() {
		return menuIndex;
	}
	public void setMenuIndex(Long menuIndex) {
		this.menuIndex = menuIndex;
	}
	
	@Column(name="menu_level")
	public Long getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(Long menuLevel) {
		this.menuLevel = menuLevel;
	}
	
	@Column(name="enable")
	public Long getEnable() {
		return enable;
	}
	public void setEnable(Long enable) {
		this.enable = enable;
	}
	
}
