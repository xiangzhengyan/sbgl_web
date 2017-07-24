package com.zfysoft.platform.model.sb;


/**
 * @author xiangzy
 * @date 2015-9-16
 *
 */
//@Entity
//@Table(name = "sb_setpowdata_log")
public class MSetPowData{
//	$SetPowData,风扇启动温度,告警温度值,告警电流值,告警反射值*校验值<CR><LF>
	
	//@Column(name="fan_temp")
	private String fanTemp;//1
	
	
	//@Column(name="warn_temp")
	private String warnTemp;//2
	
	//@Column(name="warn_curr")
	private String warnCurr;//3
	
	//@Column(name="warn_ref")
	private String warnRef;//4

	public String getFanTemp() {
		return fanTemp;
	}

	public void setFanTemp(String fanTemp) {
		this.fanTemp = fanTemp;
	}

	public String getWarnTemp() {
		return warnTemp;
	}

	public void setWarnTemp(String warnTemp) {
		this.warnTemp = warnTemp;
	}

	public String getWarnCurr() {
		return warnCurr;
	}

	public void setWarnCurr(String warnCurr) {
		this.warnCurr = warnCurr;
	}

	public String getWarnRef() {
		return warnRef;
	}

	public void setWarnRef(String warnRef) {
		this.warnRef = warnRef;
	}
	
	
	
	

}
