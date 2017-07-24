package com.zfysoft.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.zfysoft.platform.cache.XzqhCache;

/**
 * 行政区划
 * @author hudt
 * @date 2014-12-08
 */
@Entity
@Table(name="P_XZQH")
public class Xzqh {
	
	@Id
	@GeneratedValue(generator = "all_native")
	@GenericGenerator(strategy = "native", name = "all_native")
	@Column(name = "id", unique = true, nullable = false, precision = 12, scale = 0)
	private Long id;
	
	@Column(name="MC")
	private String mc;
	
	@Column(name="DWDM")
	private String dwdm;
	
	@Column(name="lx")
	private String lx;
	
	@Column(name="child")
	private Long child;//下一级节点个数
	
	@Transient
	private String compMc;//完整名称
	
	public String getCompMc() {
		compMc = XzqhCache.getCompleteMc(dwdm);
		return compMc;
	}

	public void setCompMc(String compMc) {
		this.compMc = compMc;
	}

	@Transient
	private Long objCount;//含有统计数据个数（如监测单元个数）

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getDwdm() {
		return dwdm;
	}

	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public Long getChild() {
		return child;
	}

	public void setChild(Long child) {
		this.child = child;
	}

	public Long getObjCount() {
		return objCount;
	}

	public void setObjCount(Long objCount) {
		this.objCount = objCount;
	}
	
}

