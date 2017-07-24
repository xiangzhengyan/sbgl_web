package com.zfysoft.platform.model.sb;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zfysoft.platform.model.Organization;

/**
 * @author xiangzy
 * @date 2015-9-6
 *
 */

@Entity
@Table(name = "sb_sb")
public class SB {
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 12, scale = 0)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="code")
	private String code;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", nullable = true)
	private Organization org;
	
	@Column(name="lon")
	private String lon;//经度
	
	@Column(name="lot")
	private String lot;//纬度
	
	@Column(name="mgr_persion")
	private String mgrPersion; //负责人
	
	@Column(name="phone")
	private String phone; //联系电话
	
	
	
	@Column(name="install_time")//安装时间
	private Timestamp installTime;
	
	@Column(name="addr")
	private String addr; //地址
	
	
	
	@Column(name="remarks")//备注
	private String remarks;
	
	private int status;//状态:0 未激活，1正常，2开机，3关机。。。待定
	
	@Column(name="last_time")
	private Timestamp lastUploadTime; //最后上传数据时间
	
	@Column(name="passowrd")
	private String password;
	
	@Transient
	private Boolean alarm;
	
	@Transient
	private Timestamp lastTime;//最后通信时间
	
	//-------------------
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public String getMgrPersion() {
		return mgrPersion;
	}

	public void setMgrPersion(String mgrPersion) {
		this.mgrPersion = mgrPersion;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getInstallTime() {
		return installTime;
	}

	public void setInstallTime(Timestamp installTime) {
		this.installTime = installTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getLastUploadTime() {
		return lastUploadTime;
	}

	public void setLastUploadTime(Timestamp lastUploadTime) {
		this.lastUploadTime = lastUploadTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getAlarm() {
		return alarm;
	}

	public void setAlarm(Boolean alarm) {
		this.alarm = alarm;
	}

	public Timestamp getLastTime() {
		return lastTime;
	}

	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
