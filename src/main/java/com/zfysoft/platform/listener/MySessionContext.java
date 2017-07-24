package com.zfysoft.platform.listener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zfysoft.platform.model.User;


public class MySessionContext {
	
	private static String strs ="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static  List<MySession> list = new ArrayList<>();;

	private static Random r = new Random();
	
	public static MySession addSession(){
		long curr = System.currentTimeMillis();
		
		for (int i = list.size()-1; i >=0; i--) {
			MySession sess = list.get(i);
			if(curr - sess.getTime()>3600000){
				list.remove(i);
			}else{
				break;
			}
		}
		
		MySession s = new MySession();
		s.setId(genId(32));
		s.setTime(System.currentTimeMillis());
		list.add(0,s);
		return s;
	}
	
	public static MySession getSession(String id){
	
		for (MySession s : list) {
			if(s.getId().equals(id)){
				return s;
			}
		}
		return null;
	}
	
	public static void removeSession(String id){
		
		for (MySession s : list) {
			if(s.getId().equals(id)){
				list.remove(s);
				return;
			}
		}
	}
	
	public static String genId(int n){
		char[] chars = new char[n];
		for (int i = 0; i < n; i++) {
			chars[i] = strs.charAt(r.nextInt(36));
		}
		return new String(chars);
	}
	
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 12; i++) {
			System.out.println(genId(32));
			
			
		}
	}
}
	