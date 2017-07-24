package com.zfysoft.platform.listener;

import java.util.HashMap;
import java.util.Map;

import com.zfysoft.platform.model.User;

public class MySession {
	private String id;
	private long time;
	private Map<String,Object> map = new HashMap<String,Object>();;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setAttribute(String key,Object value){
		map.put(key, value);
	}
	
	public Object getAttribute(String key){
		return map.get(key);
	}

}