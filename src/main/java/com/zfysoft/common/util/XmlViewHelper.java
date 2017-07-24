/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.common.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

/**
 * 将XML文本输出到response中
 * @author chenhm
 * @date 2013-7-5
 */
public class XmlViewHelper {

	public static void print(HttpServletResponse response, String xmlText) throws IOException {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Pragma", "No-cache");// HTTP 1.1
		response.setHeader("Cache-Control", "no-cache");// HTTP 1.0
		response.setHeader("Expires", "0");// 防止被proxy
		response.getWriter().write(xmlText);
		response.getWriter().close();
	}
	
	public static Document readXML(String filePath) throws IOException, DocumentException{
          SAXReader reader = new SAXReader();
          OutputFormat format = OutputFormat.createPrettyPrint();
          format.setEncoding("UTF-8");// 设置XML文件的编码格式
          File file = new File(XmlViewHelper.class.getResource(filePath).getFile());
          Document document = null;
          if (file.exists()) {
              document = reader.read(file);// 读取XML文件
          }
		return document;
	}
	
}
