package com.zfysoft.platform.model.sb;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sb_heart")
public class Heart {

	@Id
	@GeneratedValue(generator = "all_native") 
	@GenericGenerator(strategy = "native", name = "all_native")
	@Column(name="id", unique = true, nullable = false, precision = 12, scale = 0)
	protected Long id;

	@Column(name="code")
	protected String code;
	
	@Column(name="time")
	private Timestamp time;
	
	@Column(name="status")
	private int status;

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

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
