package com.zfysoft.platform.model.sb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author xiangzy
 * @date 2015-9-13
 *
 */
//@Entity
//@Table(name = "sb_powdata_log")
public class MPowData {

//	$PowData,1功放模块数量,2功放下标,3功放功率,4反射功率,5 50V电压,6功放1电流值,7功放1温度值,8功放2电流值,9功放2温度值*校验值 
	
	//@Column(name="amp_count")
	private String ampCount;//1整机输出功率
	
	//@Column(name="amp_num")
	private String ampNum;//2功放下标
	
	//@Column(name="amp_power")
	private String ampPower; //3功放功率
	
	//@Column(name="ref_power")
	private String refPower;//4反射功率
	
	//@Column(name="vol50")
	private String vol50;//5 50V电压
	
	//@Column(name="amp_current_1")
	private String ampCurrent1;//6功放1电流值
	
	//@Column(name="amp_temp_1")
	private String ampTemp1;//7功放1温度值
	
	//@Column(name="amp_current_2")
	private String ampCurrent2;//8功放2电流值
	
	//@Column(name="amp_temp_2")
	private String ampTemp2;//9功放2温度值

	public String getAmpCount() {
		return ampCount;
	}

	public void setAmpCount(String ampCount) {
		this.ampCount = ampCount;
	}

	public String getAmpNum() {
		return ampNum;
	}

	public void setAmpNum(String ampNum) {
		this.ampNum = ampNum;
	}



	public String getAmpPower() {
		return ampPower;
	}

	public void setAmpPower(String ampPower) {
		this.ampPower = ampPower;
	}

	public String getRefPower() {
		return refPower;
	}

	public void setRefPower(String refPower) {
		this.refPower = refPower;
	}

	public String getVol50() {
		return vol50;
	}

	public void setVol50(String vol50) {
		this.vol50 = vol50;
	}

	public String getAmpCurrent1() {
		return ampCurrent1;
	}

	public void setAmpCurrent1(String ampCurrent1) {
		this.ampCurrent1 = ampCurrent1;
	}

	public String getAmpTemp1() {
		return ampTemp1;
	}

	public void setAmpTemp1(String ampTemp1) {
		this.ampTemp1 = ampTemp1;
	}

	public String getAmpCurrent2() {
		return ampCurrent2;
	}

	public void setAmpCurrent2(String ampCurrent2) {
		this.ampCurrent2 = ampCurrent2;
	}

	public String getAmpTemp2() {
		return ampTemp2;
	}

	public void setAmpTemp2(String ampTemp2) {
		this.ampTemp2 = ampTemp2;
	}


	

}
