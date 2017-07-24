package com.zfysoft.platform.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import com.zfysoft.common.util.StringUtil;

/**
 * 文件工具类
 * 
 * @author niem
 * @2014-9-29
 */
public class FileUtil {
	/**
	 * 下载指定url地址的文件
	 */
	public static void downloadFile(HttpServletResponse response, String urlstr, String filename) throws Exception {
		// 文件名
		String ext = urlstr.substring(urlstr.lastIndexOf("."));
		if(StringUtil.isEmptyOrNull(filename)){
			String name = urlstr.substring(urlstr.lastIndexOf("/")+1,urlstr.lastIndexOf("."));
			filename = name+StringUtil.generateRandomFilename()+ext;
		}else{
			filename = filename+StringUtil.generateRandomFilename()+ext;
		}
		
		System.out.println(urlstr);
		System.out.println(filename);
		// 构造URL
		URL url = new URL(urlstr);
		// 打开连接
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.connect();
		// 输入流
		InputStream is = con.getInputStream();
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		OutputStream os = response.getOutputStream();
		try {
			response.setContentType("application/vnd.ms-excel");
	        response.setHeader("Content-Disposition", "attachment;filename=\""
	                + URLEncoder.encode(filename, "UTF-8") + "\"");
	        response.setHeader("Cache-Control",
	                "must-revalidate, post-check=0, pre-check=0");
	        response.setHeader("Pragma", "public");
	        response.setDateHeader("Expires", 0);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (os != null) {
				os.flush();
				os.close();
			}
			if(is != null){
				is.close();
			}
		}
	}
	public static void downloadFile(HttpServletResponse response, String urlstr) throws Exception {
		downloadFile(response,urlstr,null);
	}
}
