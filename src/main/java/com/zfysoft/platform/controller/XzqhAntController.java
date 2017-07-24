package com.zfysoft.platform.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.Xzqh;
import com.zfysoft.platform.service.XzqhService;

@Controller
@RequestMapping("/xzqhAnt/*")
public class XzqhAntController {

	@Resource
	private XzqhService xzqhService;

	// 暂时注释，以免调用到导致数据出错
	@ResponseBody
	@RequestMapping("/analysis.do")
	public ResultData analysis(HttpServletRequest request) {
		try {
			List<Xzqh> list = new ArrayList<Xzqh>();

			String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2013/index.html";
			Document doc = Jsoup.connect(url).timeout(1000)
					.userAgent("Mozilla").get();
			Element body = doc.body();
			List<Element> trs = body.select("tr.provincetr");
			for (Element tr : trs) {
				List<Element> tds = tr.select("td");
				for (Element td : tds) {
					String mc = td.select("a").get(0).text().trim();
					String href = td.select("a").get(0).attr("href");
					String dwdm = href.replace(".html", "");
					String url1 = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2013/"
							+ href;
					System.out.println(dwdm + " " + mc + " " + url1);
					Xzqh xzqh = new Xzqh();
					xzqh.setDwdm(dwdm);
					xzqh.setMc(mc);
					xzqh.setLx("1");
//					TODO
					xzqhService.saveXzqh(xzqh);

					
					getHTML(xzqh,url1, null);
				}
			}

			return new ResultData(ResultData.SUCCESS, null, null, list.size());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultData(ResultData.ERROR, e.getMessage(), null, e);
		}
	}



	/***
	 * 获取HTML内容
	 * 
	 * @return HTML String
	 * @throws Exception
	 */
	private void getHTML(Xzqh pxzqh,String url, String trclass)
			throws Exception {
		Document doc = Jsoup.connect(url).timeout(1000).userAgent("Mozilla")
				.get();
		Element body = doc.body();
		String trclassnext = "";
		String lx = "1";
		int len = 0;
		if (trclass == null) {
			trclassnext = "citytr";
			lx = "2";
			len = 4;
		} else if (trclass == "citytr") {
			trclassnext = "countytr";
			lx = "3";
			len = 6;
			return;
		} else if (trclass == "countytr") {
			trclassnext = "towntr";
			lx = "4";
			len = 9;
			return;
		} else if (trclass == "towntr") {
			trclassnext = "villagetr";
			lx = "5";
			len = 12;
			return;
		}else{
			return;
		}
		Elements trs = body.select("tr." + trclassnext);
		if(pxzqh!=null){
			pxzqh.setChild((long)trs.size());
		}
		for (Element tr : trs) {
			Xzqh xzqh = new Xzqh();
			
			Elements tds = tr.select("td");
			int i = 0;
			String Dwdm = "";
			String mc = "";
			for (Element td : tds) {

				if (trclassnext == "villagetr") {
					if (i == 0) {
						Dwdm = td.html().trim();
						System.out.print(td.html());
					} else if (i == 2) {
						mc = td.html().trim();
						System.out.print(td.html());
					}
				}

				if (trclassnext != "villagetr") {
					if (td.select("a").size() > 0) {
						if (i == 0) {
							String lastUrl = url.substring(0,
									url.lastIndexOf("/") + 1);
							getHTML(xzqh,lastUrl
									+ td.select("a").get(0).attr("href"),
									trclassnext);
							Dwdm = td.select("a").get(0).html().trim()
									.substring(0, len);
							System.out.print(Dwdm);
						} else {
							mc = td.select("a").get(0).html().trim();
							System.out.print(mc);
						}
					} else {

						if (i == 0) {
							Dwdm = td.html().trim().substring(0, len);
							System.out.print(Dwdm);
						} else {
							mc = td.html().trim();
							System.out.print(mc);
						}

					}
				}
				i++;
			}

			
			xzqh.setDwdm(Dwdm);
			xzqh.setMc(mc);
			xzqh.setLx(lx);
//			TODO
			xzqhService.saveXzqh(xzqh);
			System.out.println();
		}
	}
	public static void main(String[] args) throws Exception {
		String strURL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2013/53.html";
		XzqhAntController c = new XzqhAntController();
		c.analysis(null);
	}
}
