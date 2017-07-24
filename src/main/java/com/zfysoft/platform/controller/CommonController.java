package com.zfysoft.platform.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.UploadImages;
import com.zfysoft.platform.service.TpService;

/**
 * 杂七杂八的东西放在这里
 * @author hudt
 */
@Controller
@RequestMapping("/common/*")
public class CommonController {
	
	@Resource
	private TpService tpService;
	
	private static Logger logger = Logger.getLogger(CommonController.class);  
	
	/**
	 * 上传图片
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/uploadFile2.do")
	public String uploadFile2(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out = null;
		//转型为MultipartHttpRequest(重点的所在)  
		MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;
		//获得第1张图片（根据前台的name名称得到上传的文件）   
		MultipartFile imgFile  =  multipartRequest.getFile("imgFile");
		System.out.println(imgFile.getOriginalFilename()+"....");
		//定义一个数组，用于保存可上传的文件类型  
		List<String> fileTypes = new ArrayList<String>();  
		fileTypes.add("jpg");  
		fileTypes.add("jpeg");  
		fileTypes.add("bmp");  
		fileTypes.add("gif");
		fileTypes.add("png");
		Map<String, String> result = new HashMap<String, String>();
		try {
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			//保存第一张图片  
			if(!(imgFile.getOriginalFilename() ==null || "".equals(imgFile.getOriginalFilename()))) {  
				File file = this.getFile(imgFile,fileTypes);
				if(file==null){//格式不正确
					result.put("exNotMatch", "no_format");
				}else{
					result.put("filePath", file.getName());
					result.put("originalFileName", imgFile.getOriginalFilename());
					result.put("success", "上传成功");
				}
			} else{
				result.put("error", "error");
			}
			out.print(JSONObject.fromObject(result));
		} catch (Exception e) {
			logger.error("上传图片失败", e);
			out.print(JSONObject.fromObject(result));
		} finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	/**
	 * 通过传入页面读取到的文件，处理后保存到本地磁盘，并返回一个已经创建好的File 
	 * @param fileTypes 允许的文件扩展名集合 
	 * @return 
	 */  
	private File getFile(MultipartFile imgFile,List<String> fileTypes) {

		String fileName = imgFile.getOriginalFilename();
		//获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名  
		String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()); 
		//对扩展名进行小写转换  
		ext = ext.toLowerCase(); 
		File file = null;  
		if(fileTypes.contains(ext)) {                      //如果扩展名属于允许上传的类型，则创建文件  
			//重命名文件名
			fileName = new Date().getTime()+"."+ext;
			file = this.creatFolder(fileName);  
			try {  
				imgFile.transferTo(file);                   //保存上传的文件  
			} catch (IllegalStateException e) {  
				e.printStackTrace();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}else{
			return null;
		}  
		return file;
	}


	private File creatFolder(String fileName){
		File file = null;  
		String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
		File temp = new File(path);
		path = temp.getParent()+File.separator+"upload"+File.separator+"img";
		System.out.println(path);
		File firstFolder = new File(path);
		if(!firstFolder.exists()){
			firstFolder.mkdirs();
		}
		file = new File(firstFolder,fileName);

		return file;
	}
	
	@ResponseBody
	@RequestMapping("/getImageSize.do")
	public ResultData getImageSize(HttpServletRequest request,Long id){
		try {
			UploadImages image = tpService.getById(id);
			if(StringUtil.isEmptyOrNull(image) || StringUtil.isEmptyOrNull(image.getImageName())){
				return new ResultData(ResultData.ERROR,"无法查询到图片");
			}
			String imageName = image.getImageName();
			
			File file = new File(request.getSession().getServletContext().getRealPath(""));
			String webapps = file.getParent();
			String imageFullUrl = webapps + "/upload/img/" + imageName;
			
			File picture = new File(imageFullUrl);  
	        BufferedImage sourceImg =ImageIO.read(new FileInputStream(picture));   
	        
	        JSONObject json = new JSONObject();
	        json.element("filesize", String.format("%.1f",picture.length()/1024.0));
	        json.element("width", sourceImg.getWidth());
	        json.element("height", sourceImg.getHeight());
			
			return new ResultData(ResultData.SUCCESS,null,null,json);
		} catch (Exception e) {
			logger.error("获取图片大小出错", e);
			return new ResultData(ResultData.ERROR,"获取图片大小出错");
		}
	}
}
