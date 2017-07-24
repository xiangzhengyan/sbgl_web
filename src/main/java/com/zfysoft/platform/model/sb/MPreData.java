package com.zfysoft.platform.model.sb;


/**
 * @author xiangzy
 * @date 2015-9-13
 *
 */

public class MPreData{

//	$PreData,整机输出功率,整机反射功率,AGC电压,前级输入功率,前级输出电平,系统温度,衰减值,整机反射门限值,整机输入下限,整机输入上限*校验值<CR><LF>
	
	//@Column(name="total_out_power")
	private String totalOutPower;//1整机输出功率
	
	//@Column(name="total_ref_power")
	private String totalRefPower;//2整机反射功率
	
	//@Column(name="agc_vol")
	private String agcVol; //3AGC电压
	
	//@Column(name="pre_in_power")
	private String preInPower;//4前级输入功率
	
	//@Column(name="pre_out_level")
	private String preOutLevel;//5前级输出电平
	
	//@Column(name="sys_temp")
	private String sysTemp;//6系统温度,
	
	//@Column(name="pad_value")
	private String padValue;//7衰减值
	
	//@Column(name="total_ref_limit")
	private String totalRefLimit;//8整机反射门限值
	
	//@Column(name="total_in_lower_limit")
	private String totalInLowerLimit;//9整机输入下限

	//@Column(name="total_in_upper_limit")
	private String totalInUpperLimit;//10整机输入上限

	public String getTotalOutPower() {
		return totalOutPower;
	}

	public void setTotalOutPower(String totalOutPower) {
		this.totalOutPower = totalOutPower;
	}

	public String getTotalRefPower() {
		return totalRefPower;
	}

	public void setTotalRefPower(String totalRefPower) {
		this.totalRefPower = totalRefPower;
	}

	public String getAgcVol() {
		return agcVol;
	}

	public void setAgcVol(String agcVol) {
		this.agcVol = agcVol;
	}

	public String getPreInPower() {
		return preInPower;
	}

	public void setPreInPower(String preInPower) {
		this.preInPower = preInPower;
	}

	public String getPreOutLevel() {
		return preOutLevel;
	}

	public void setPreOutLevel(String preOutLevel) {
		this.preOutLevel = preOutLevel;
	}

	public String getSysTemp() {
		return sysTemp;
	}

	public void setSysTemp(String sysTemp) {
		this.sysTemp = sysTemp;
	}

	public String getPadValue() {
		return padValue;
	}

	public void setPadValue(String padValue) {
		this.padValue = padValue;
	}

	public String getTotalRefLimit() {
		return totalRefLimit;
	}

	public void setTotalRefLimit(String totalRefLimit) {
		this.totalRefLimit = totalRefLimit;
	}

	public String getTotalInLowerLimit() {
		return totalInLowerLimit;
	}

	public void setTotalInLowerLimit(String totalInLowerLimit) {
		this.totalInLowerLimit = totalInLowerLimit;
	}

	public String getTotalInUpperLimit() {
		return totalInUpperLimit;
	}

	public void setTotalInUpperLimit(String totalInUpperLimit) {
		this.totalInUpperLimit = totalInUpperLimit;
	}
	


}
