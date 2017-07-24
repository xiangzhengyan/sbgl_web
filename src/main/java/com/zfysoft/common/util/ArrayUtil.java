/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author hudt
 * @date 2013-7-4
 */
public class ArrayUtil {
	
	/**
	 * Date类型会转换成yyyy-MM-dd的String类型，其他类型会调用toString方法
	 * 会调用obj的toString方法
	 * @param obj
	 * @return
	 */
	public static String[] objsToStrings(Object ...params){
		int length = params.length;
		String[] strs = new String[length];
		for(int i = 0; i < length ; i++){
			String type =  params[i].getClass().getSimpleName();
			if(type.equals("Date")){
				strs[i] = DateUtil.dateToString((Date)params[i], "yyyy-MM-dd");
			}else {
				strs[i] = params[i].toString();
			}
			strs[i] = params[i].toString();
		}
		return strs;
	}
	
	/**
	 * 从String数组转成Long数组
	 */
	public static Long[] strsToLongArray(String[] strs){
		int len = strs.length;
		Long[] array = new Long[len];
		for(int i = 0 ; i < len ; i++){
			if(strs[i]==null || strs[i].trim().equals("")){
				array[i] = null;
			}else {
				array[i] = Long.valueOf(strs[i]);
			}
		}
		return array;
	}
	
	/**
	 * 数组转成xx隔开
	 */
	public static String arrayToComma(String[] array,String comma){
		String rtn = "";
		comma = comma==null?",":comma;
		if(array == null){
			return "";
		}
		for(String str : array){
			rtn += str + comma;
		}
		if(rtn.length() >= comma.length()){
			rtn = rtn.substring(0, rtn.length()-comma.length());
		}
		return rtn;
	}
	
	/**
	 * 数组转成逗号隔开
	 */
	public static String arrayToComma(String[] array){
		return arrayToComma(array,",");
	}
	
	/**
	 * List 做减法
	 * @param <E>
	 */
	public static <E> List<E> minusList(List<E> bigList,List<E> smallList){
		List<E> rtn = new ArrayList<E>();
		for(E e : bigList){
			if(!smallList.contains(e)){
				rtn.add(e);
			}
		}
		return rtn;
	}
	
	public static Object[] plus(Object[] ...objsArray){
		List<Object> rtn = new ArrayList<Object>();
		for(Object[] objs : objsArray){
			for(Object obj :objs){
				rtn.add(obj);
			}
		}
		return rtn.toArray();
	}
	
	public static Set<?> arrToSet(Object[] objs){
		Set<Object> s = new HashSet<Object>();
		for(Object o : objs){
			s.add(o);
		}
		return s;
	}
	
	/**
	 * 将集合List、Set转换为以逗号隔开的字符串 mapJson格式 重载
	 * @param <T>
	 * 
	 * @param stringList
	 * @return
	 */
	public static <T>String  collectionToString(T t){
		return collectionToString(t,null);
	}
	
	/**
	 * 将集合List、Set转换为以逗号隔开的字符串 mapJson格式
	 * @param <T>
	 * 
	 * @param stringList
	 * @return
	 */
	public static <T>String  collectionToString(T t,String reg) {
		if(StringUtil.isEmptyOrNull(reg)){
			reg = ",";
		}
		if (t == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		for(Object o : (Collection<?>) t) {
			if (flag) {
				result.append(reg);
			} else {
				flag = true;
			}
			result.append(o);
		}
		return result.toString();
	}
	
	/**
	 * 得到2个集合中的相同元素转为字符串
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static <T>String getTwoCollectionEqEStr(T t1,T t2){
		return collectionToString(getTwoCollectionEqEList(t1,t2));
	}
	
	/**
	 * 得到2个集合中的相同元素组成的集合
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static <T>Set<Object> getTwoCollectionEqEList(T t1,T t2){
		Set<Object> s = new HashSet<Object>();//存放相同元素
		for(Object o:(Collection<?>) t1){
			if(eInCollectionOrNotRepeat(t2,o)){
				s.add(o);
			}
		}
		return s;
	}
	
	
	/**
	 * 元素是否在集合中存在
	 * @param t
	 * @param o
	 * @return
	 */
	public static <T>Boolean eInCollectionOrNotRepeat(T t,Object o){
		Boolean boo = false;
		if(((Collection<?>)t).contains(o)){
			boo = true;
		}
		return boo;
	}
	
	/**
	 * 判断obj是否在array中
	 * @param array
	 * @param obj
	 * @return
	 */
	public static boolean isInArray(Object[] array,Object obj){
		for(Object objInArray : array){
			if(objInArray.equals(obj)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断2个集合是否相同(集合每个元素不能为空)
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static boolean twoListEquals(List<?> list1,List<?> list2){
		if(list1.size()!=list2.size()){
			return false;
		}
		boolean boo = false;
		Map<String,Integer> map = new HashMap<String,Integer>();
		int i=0;
		for(Object obj1 : list1){		 
			 map.put(obj1.toString().trim(), i++);
		}
		for(Object obj2 : list2){
			boo = false;
			if(map.get(obj2.toString().trim())!=null){
				boo = true;
				continue;
			}else{
				break;
			}
		}
		return boo;
	}
	
	/**
	 * 判断map的value全为空白
	 * @param map
	 * @return
	 */
	public static boolean mapAllValueIsEmptyOrNull(Map<?,?> map){
		boolean boo=true;
		if(map==null||map.size()==0){
			return boo;
		}
		for(Object key:map.keySet()){
			if(StringUtil.isNotEmptyOrNull(map.get(key))){
				boo=false;
				break;
			}
		}
		return boo;
	}
	
	/**
	 * 判断map的value全为null或空字符串或数字0
	 * @param map
	 * @return
	 */
	public static boolean mapAllValueIsEmptyOrNullOr0(Map<?,?> map){
		boolean boo=true;
		if(map==null||map.size()==0){
			return boo;
		}
		for(Object key:map.keySet()){
			if(StringUtil.isNotEmptyOrNull(map.get(key))&&NumberUtil.isNotEquals0(map.get(key))){
				boo=false;
				break;
			}
		}
		return boo;
	}
	
	/**
	 * 判断元素在集合中出现的次数
	 * @param list
	 * @param element
	 * @return
	 */
	public static Integer eInListTimes(List<?> list,Object element){
		if(list==null||list.size()==0){
			return 0;
		}
		Map<Object,Integer> map = new HashMap<Object,Integer>();
		int i = 1;
		for(Object o : list){
			if(StringUtil.isEmptyOrNull(map.get(o))){
				map.put(o, i);
			}else{
				map.put(o, map.get(o)+1);
			}
		}
		return StringUtil.isEmptyOrNull(map.get(element))?0:map.get(element);
	}
	/**
	 * 判断元素是否在集合中出现次数
	 * @param list
	 * @param e
	 * @return
	 */
	public static List<String> eInListAppaer(List<String> list, String e){
		if(list==null||list.size()==0||StringUtil.isEmptyOrNull(e)){
			return null;
		}
		List<String> eList = new ArrayList<String>();
		String reg = StringUtil.getReg(e, StringUtil.regs);
		String[] arr1 = null;
		if(StringUtil.isEmptyOrNull(reg)){
			arr1 =new String[]{e};
		}else{
			arr1 = e.split(reg);
		}
		for(String s1:arr1){
			for(String e2:list){
				reg = StringUtil.getReg(e2, StringUtil.regs);
				String[] arr2 = null;
				if(StringUtil.isEmptyOrNull(reg)){
					arr2 = new String[]{e2};
				}else{
					arr2 = e2.split(reg);
				}
				for(String s2:arr2){
					if(s1.equals(s2)){
						eList.add(s1);
					}
				}
			}
		}
		return eList;
	}
	/**
	 * 找到List中的重复出现的元素
	 * @param list
	 * @return
	 */
	public static Set<?> eInListRepeate	(List<String> list){
		if(list==null||list.size()==0){
			return null;
		}
		Set<Object> set = new HashSet<Object>();
		Map<String,Integer> map = new HashMap<String,Integer>();
		Integer i;
		for(String s:list){
			i = map.get(s);
			if(StringUtil.isEmptyOrNull(i)){
				map.put(s, 1);
			}else{
				set.add(s);
			}
		}
		return set;
	}
	/**
	 * List 转 set
	 * @return
	 */
	public static Set<?> listToSet(List<?> list){
		if(list==null||list.size()==0){
			return null;
		}
		Set<Object> set = new HashSet<Object>(); 
		set.addAll(list);
		return set;
	}
	/**
	 * set 转 list
	 * @param set
	 * @return
	 */
	public static List<String> setToList(Set<String> set){
		List<String> list = new ArrayList<String>();
		list.addAll(set);
		return list;
	} 
}
