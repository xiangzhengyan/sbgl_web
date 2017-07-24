package com.zfysoft.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.zfysoft.platform.listener.RevisionListener;


@Entity
@Table(name = "P_REVINFO")
@RevisionEntity(RevisionListener.class)
public class RevInfo {
	@Id
	@GeneratedValue
	@RevisionNumber
	@Column(name = "REV")
	private int rev;

	@RevisionTimestamp
	@Column(name = "REVTSTMP")
	private long revtstmp;


	private String userId;
	
	private String ip;

	@Column(name = "USERID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRev() {
		return rev;
	}

	public void setRev(int rev) {
		this.rev = rev;
	}

	public long getRevtstmp() {
		return revtstmp;
	}

	public void setRevtstmp(long revtstmp) {
		this.revtstmp = revtstmp;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	

}