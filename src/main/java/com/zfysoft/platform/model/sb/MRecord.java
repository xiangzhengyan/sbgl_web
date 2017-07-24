package com.zfysoft.platform.model.sb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangzy
 * @date 2015-9-21
 * 
 */
public class MRecord {
	private Timestamp time;
	private String code;
	private String type;
	private String[] preData;
	private String[] setPowData;
	private List<String[]> powDataList = new ArrayList<String[]>();
	private Boolean alarm;

	public static MRecord from(DataLog log){
		if(log==null){
			return null;
		}
		MRecord record = parse(log.getValue());
		if(record==null){
			return null;
		}
//		record.setCode(log.getCode());
		record.setTime(log.getTime());
		record.setAlarm("Alarm".equals(log.getType()));
		record.setType(log.getType());
		return record;
	}
	
	// 转换成现实数据，
	public static MRecord parse(String data) {
		// PreData,1,2,3,4,5,6,7,8,9,10,PowData,1,2,3,4,5,6,7,8,9,PowData,1,2,3,4,5,6,7,8,9,PowData,1,2,3,4,5,6,7,8,9,PowData,1,2,3,4,5,6,7,8,9,SetPowData,1,2,3,4
		// PreData,1,2,3,4,5,6,7,8,9,10,PowData,1,2,3,4,5,6,7,8,9,SetPowData,1,2,3,4
		try {
			MRecord record = new MRecord();
			String[] datas = data.replace("PreData,", "").split(",PowData,|,SetPowData,");
			for (int i = 0; i < datas.length; i++) {
				String[] vals = datas[i].split(",");

				if (i == 0) {
					//predata
					if(vals.length!=10){
						return null;
					}
					
					for (int j = 0; j < vals.length; j++) {
						if(j==0||j==6){
							
						}else{
							vals[j] = Float.parseFloat(vals[j])/10 +"";
						}
					}
					
					record.setPreData(vals);
					
				} else if (i == datas.length - 1) {
					//setpowdata
					if(vals.length!=4){
						return null;
					}
					for (int j = 0; j < vals.length; j++) {
						vals[j] = Float.parseFloat(vals[j])/10 +"";
					}
					record.setSetPowData(vals);
				} else {
					//powdata
					if(vals.length!=9){
						return null;
					}
					
					for (int j = 0; j < vals.length; j++) {
						if(j==5||j==6||j==7||j==8){
						
							vals[j] = Float.parseFloat(vals[j])/10 +"";
						}
					}
					record.addPowData(vals);
				}
			}

			return record;
		} catch (Exception e) {
			System.err.println("解析dataLog异常");
			e.printStackTrace();
			return null;
		}
		
	}





	public String[] getPreData() {
		return preData;
	}





	public void setPreData(String[] preData) {
		this.preData = preData;
	}





	public String[] getSetPowData() {
		return setPowData;
	}





	public void setSetPowData(String[] setPowData) {
		this.setPowData = setPowData;
	}





	public List<String[]> getPowDataList() {
		return powDataList;
	}





	public void setPowDataList(List<String[]> powDataList) {
		this.powDataList = powDataList;
	}


	public void addPowData(String[] powData) {
		this.powDataList.add(powData);
	}


	public static void main(String[] args) {
//		String s1 = "PreData,1,2,3,4,5,6,7,8,9,10,PowData,1,2,3,4,5,6,7,8,9,PowData,1,2,3,4,5,6,7,8,9,PowData,1,2,3,4,5,6,7,8,9,PowData,1,2,3,4,5,6,7,8,9,SetPowData,1,2,3,4";
//		String s2 = "PreData,1,2,3,4,5,6,7,8,9,10,PowData,1,2,3,4,5,6,7,8,9,SetPowData,1,2,3,4";
//
//		parse(s2);
		
		int a = 3;
		char c = (char)850;
		System.out.println((int)c);
	}





	public Timestamp getTime() {
		return time;
	}





	public void setTime(Timestamp time) {
		this.time = time;
	}





	public String getCode() {
		return code;
	}





	public void setCode(String code) {
		this.code = code;
	}
	public Boolean getAlarm() {
		return alarm;
	}
	public void setAlarm(Boolean alarm) {
		this.alarm = alarm;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

	
}
