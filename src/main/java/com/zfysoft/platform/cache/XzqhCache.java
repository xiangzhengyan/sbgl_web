package com.zfysoft.platform.cache;

import java.util.ArrayList;
import java.util.List;

import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.config.SpringContextHolder;
import com.zfysoft.platform.model.Xzqh;
import com.zfysoft.platform.service.XzqhService;

/**
 * 行政区划缓存
 */
public class XzqhCache {
	
	private static List<Xzqh> list = new ArrayList<Xzqh>();
	
//	private static List<Xzqh> shengs = new ArrayList<Xzqh>();
//	private static List<Xzqh> zhous = new ArrayList<Xzqh>();
//	private static List<Xzqh> xians = new ArrayList<Xzqh>();
//	private static List<Xzqh> zhens = new ArrayList<Xzqh>();
//	private static List<Xzqh> cuns = new ArrayList<Xzqh>();
	
	private static XzqhService xzqhService;
	
	static{
		xzqhService = SpringContextHolder.getBean("xzqhService");
	}
	
	/**
	 * 加载所有数据库中的行政区划
	 */
	public static void loadAll(){
		list = xzqhService.getAllXzqh();
//		shengs = getListByLevel(1);
//		zhous = getListByLevel(2);
//		xians = getListByLevel(3);
//		zhens = getListByLevel(4);
//		cuns = getListByLevel(5);
	}
	
	/**
	 * 根据单位代码获取行政区划
	 * @param dwdm
	 * @return
	 */
	public static Xzqh getByDwdm(String dwdm){
		if(StringUtil.isEmptyOrNull(dwdm)){
			return null;
		}
		for(Xzqh xzqh : list){
			if(dwdm.equals(xzqh.getDwdm())){
				return xzqh;
			}
		}
		return null;
	}
	
	/**
	 * 根据单位代码获取行政区划
	 * @param dwdm
	 * @return
	 */
	public static String getMcByDwdm(String dwdm){
		if(StringUtil.isEmptyOrNull(dwdm)){
			return "";
		}
		for(Xzqh xzqh : list){
			if(dwdm.equals(xzqh.getDwdm())){
				return xzqh.getMc();
			}
		}
		return "";
	}
	
	/**
	 * 根据名称搜索行政区划
	 */
	public static List<Xzqh> search(String mc){
		List<Xzqh> rtnlist = new ArrayList<Xzqh>();
		for(Xzqh xzqh : list){
			if(xzqh.getMc().contains(mc)){
				rtnlist.add(xzqh);
			}
		}
		return rtnlist;
	}
	
	
	/**
	 * 根据父级单位代码获取下级行政区划
	 * @param dwdm
	 * @return
	 */
	public static List<Xzqh> getSubsByDwdm(String dwdm){
		
		List<Xzqh> rtnlist = new ArrayList<Xzqh>();
		if(StringUtil.isEmptyOrNull(dwdm)){
			return rtnlist;
		}
		int level = getLevel(dwdm);
		//参数级别为5（村），不可能有子项
		if(level == 5 || level == 100){
			return rtnlist;
		}
	
		
	
		for(Xzqh xzqh : list){
			if(level==0 && xzqh.getLx().equals("1")){
				rtnlist.add(xzqh);
			}else if(xzqh.getDwdm().startsWith(dwdm) && Integer.parseInt(xzqh.getLx()) == level+1){
				rtnlist.add(xzqh);
			}
		}
		return rtnlist;
	}
	
	public static List<Xzqh> getListByLevel(int level){
		List<Xzqh> rtnlist = new ArrayList<Xzqh>();
		if(level <1 || level >5){
			return rtnlist;
		}
		for(Xzqh xzqh : rtnlist){
			if(Integer.parseInt(xzqh.getLx()) == level){
				rtnlist.add(xzqh);
			}
		}
		return rtnlist;
	}
	
	/**
	 * 获取完整的行政区划名称
	 * @param unitCode
	 */
	public static String getCompleteMc(String dwdm){
		if(StringUtil.isEmptyOrNull(dwdm)){
			return "";
		}
		int level = getLevel(dwdm);
		if(level == 1){
			String sheng = getMcByDwdm(dwdm.substring(0, 2));
			return sheng;
		}else if(level == 2){
			String sheng = getMcByDwdm(dwdm.substring(0, 2));
			String zhou = getMcByDwdm(dwdm.substring(0, 4));
			return sheng+zhou;
		}else if(level == 3){
			String sheng = getMcByDwdm(dwdm.substring(0, 2));
			String zhou = getMcByDwdm(dwdm.substring(0, 4));
			String xian = getMcByDwdm(dwdm.substring(0, 6));
			return sheng+zhou + xian;
		}else if(level == 4){
			String sheng = getMcByDwdm(dwdm.substring(0, 2));
			String zhou = getMcByDwdm(dwdm.substring(0, 4));
			String xian = getMcByDwdm(dwdm.substring(0, 6));
			String zhen = getMcByDwdm(dwdm.substring(0, 9));
			return sheng+zhou + xian + zhen;
		}
		else if(level == 5){
			String sheng = getMcByDwdm(dwdm.substring(0, 2));
			String zhou = getMcByDwdm(dwdm.substring(0, 4));
			String xian = getMcByDwdm(dwdm.substring(0, 6));
			String zhen = getMcByDwdm(dwdm.substring(0, 9));
			String cun = getMcByDwdm(dwdm.substring(0, 12));
			return sheng+zhou + xian + zhen + cun;
		}
		else{
			return "";
		}
	}
	
	
	
	/**
	 * 根据单位代码，获取行政区划级别
	 * @param dwdm
	 * @return 1省 2市州 3县区市  4乡镇 5村
	 * <br> 100 为异常结果
	 */
	public static int getLevel(String dwdm){
		if(StringUtil.isEmptyOrNull(dwdm)){
			return 100;
		}
		int len = dwdm.length();
		
		//TODO
		//单位代码长度为1 代表 level=0为中国
		if(len==1){
			return 0;
		}
		//单位代码长度为2 代表 level=1为省的
		if(len == 2){
			return 1;
		}
		//单位代码长度为4 代表 level=2为市州的
		if(len == 4){
			return 2;
		}
		//单位代码长度为6 代表 level=3为区县市的
		if(len == 6){
			return 3;
		}
		//单位代码长度为9 代表 level=4为乡镇的
		if(len == 9){
			return 4;
		}
		//单位代码长度为12 代表 level=5为村的
		if(len == 12){
			return 5;
		}
		return 100;
	}

}
