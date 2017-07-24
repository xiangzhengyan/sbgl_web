package com.zfysoft.platform.model.sb;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sb_data_log")
public class DataLog{

	@Id
	@GeneratedValue(generator = "all_native") 
	@GenericGenerator(strategy = "native", name = "all_native")
	@Column(name="id", unique = true, nullable = false, precision = 12, scale = 0)
	protected Long id;

	@Column(name="code")
	protected String code;
	
	@Column(name="time")
	private Timestamp time;
	
	@Column(name="type")
	private String type;
	
	@Column(name="value")
	private String value;
	

	

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}




	
	
	
}
