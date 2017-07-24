package com.zfysoft.platform.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.listener.MySession;
import com.zfysoft.platform.listener.MySessionContext;
import com.zfysoft.platform.model.Organization;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.model.sb.DataLog;
import com.zfysoft.platform.model.sb.Heart;
import com.zfysoft.platform.model.sb.MPreData;
import com.zfysoft.platform.model.sb.MRecord;
import com.zfysoft.platform.model.sb.MSetPowData;
import com.zfysoft.platform.model.sb.SB;
import com.zfysoft.platform.service.OrganizationService;
import com.zfysoft.platform.service.RoleService;
import com.zfysoft.platform.service.SBService;
import com.zfysoft.platform.util.Client;
import com.zfysoft.platform.util.SessionUtil;
import com.zfysoft.platform.util.TimeUtil;

@Controller
@RequestMapping("/sbgl/*")
public class SbglController {
	
	//private static final Logger log = Logger.getLogger(SbglController.class);
	
	@Resource
	private SBService sbService;
	
	@Resource
	private RoleService roleService;
	
	@Resource
	private OrganizationService organizationService;

	@RequestMapping("/sbgl.do")
	public String sbgl(HttpServletRequest request){
		return "platform/sbgl/sbgl";
	}
	
	@RequestMapping("/detail.do")
	public String pageDetail(HttpServletRequest request,Long orgId,String oper){
		
		if(!hasOrgAuth(request, orgId)){
			return "platform/sbgl/view_sb";
		}
		
		String idStr = request.getParameter("id");
		request.setAttribute("orgId", orgId);
		if (idStr==null) {
			
			return "platform/sbgl/edit_sb";
		} else {
			Long id = Long.parseLong(idStr);
			SB sb = sbService.getById(id);
			request.setAttribute("sb", sb);
			if ("edit".equals(request.getParameter("oper"))) {
				return "platform/sbgl/edit_sb";
			}else{
				return "platform/sbgl/view_sb";
			}
		}
		
	}
	
	@RequestMapping("/sbListByOrg.do")
	public String getSBListByOrg(HttpServletRequest request,Long orgId){
		
	
		if(!hasOrgAuth(request, orgId)){
			return "platform/sbgl/list";
		}
		
		List<SB> sblist = sbService.getListByOrgId(orgId);
		long currTime = System.currentTimeMillis();
		for (SB sb : sblist) {
			DataLog log = sbService.queryLastDataLog(sb.getCode());
			Heart heart = sbService.queryLastHeart(sb.getCode());
			Timestamp lastTime = new Timestamp(0);
			if(log!=null){
				if(log.getTime().after(lastTime)){
					lastTime = log.getTime();
				}
			}
			if(heart!=null){
				if(heart.getTime().after(lastTime)){
					lastTime = heart.getTime();
				}
			}
			if(lastTime.getTime()!=0){
				sb.setLastTime(lastTime);
				if(currTime-lastTime.getTime()>300000){
					sb.setAlarm(true);
				}
			}else{
				sb.setAlarm(true);
			}
			
		}
		request.setAttribute("orgId", orgId);
		request.setAttribute("list", sblist);
		return "platform/sbgl/list";
		
	}
	
	@RequestMapping("/save.do")
	@ResponseBody
	public ResultData saveSB(HttpServletRequest request,SB sb,Long orgId){
		if(!hasOrgAuth(request, orgId)){
			return new ResultData(ResultData.ERROR, "没有该组织权限");
		}
		Organization org = organizationService.queryOrgaById(orgId);
		sb.setOrg(org);
		
		sbService.save(sb);
		return new ResultData(ResultData.SUCCESS,"保存成功",ResultData.OPT_UPDATE);
	}
	
	@RequestMapping("/del.do")
	@ResponseBody
	public ResultData delSB(HttpServletRequest request,Long sbId){
		
		if(sbId==null){
			return  new ResultData(ResultData.ERROR,"设备不存在或没有权限",ResultData.OPT_DELETE);
		}
		
		SB sb = sbService.getById(sbId);
		if(sb==null || !hasOrgAuth(request, sb.getOrg().getId())){
			return  new ResultData(ResultData.ERROR,"设备不存在或没有权限",ResultData.OPT_DELETE);
		}
		
		sbService.delete(sb);
		
		return new ResultData(ResultData.SUCCESS,"删除成功",ResultData.OPT_UPDATE);
	}
	
	//检查登陆用户是否有orgId权限
	private boolean hasOrgAuth(HttpServletRequest request,Long orgId){

		if(orgId==null){
			return false;
		}
		long userId = SessionUtil.getUserId(request);
		System.out.println("userId:"+userId);
		Organization userOrg = organizationService.querySingleOrgaByUser(userId);
		List<Organization> orgList = organizationService.queryAllOrga(userOrg.getId());
		System.out.println("userOrg:"+userOrg.getId());
		for (Organization org : orgList) {
//			System.out.println("allorg:"+org.getId());
			if(orgId.longValue()==org.getId().longValue()){
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping("/viewSb.do")
	public String viewSb(HttpServletRequest request,Long id,String code){
		request.setAttribute("id",id);
		request.setAttribute("code", code);
//		DataLog log = sbService.queryLastDataLog(code);
//		MRecord record = MRecord.from(log);
//		request.setAttribute("record", record);
		
		return "platform/sbgl/view_sb";
	}
	
	@RequestMapping("/lastData.do")
	public String lastData(HttpServletRequest request,Long id,String code){
		request.setAttribute("id",id);
		request.setAttribute("code", code);
		DataLog log = sbService.queryLastDataLog(code);
		MRecord record = MRecord.from(log);
		request.setAttribute("record", record);
		
		return "platform/sbgl/view_last_data";
	}
	

	@RequestMapping("/getLastData.do")
	@ResponseBody
	public ResultData getLastData(HttpServletRequest request,String code){
		DataLog log = sbService.queryLastDataLog(code);
		MRecord record = MRecord.from(log);
		return new ResultData(ResultData.SUCCESS, "查询成功", null, record);
	}
	
	@RequestMapping("/hasNewData.do")
	@ResponseBody
	public ResultData hasNewData(HttpServletRequest request,String code,String time){
		DataLog log = sbService.queryLastDataLog(code);
		boolean hasNewData = false;
		if(log!=null){
			Timestamp t=TimeUtil.parseTime(time);
			hasNewData = log.getTime().after(t);
		}
		return new ResultData(ResultData.SUCCESS, "查询成功", null, hasNewData);
	}
	
	@RequestMapping("/mgrSb.do")
	public String mgrSb(HttpServletRequest request,Long id){
		SB sb = sbService.getById(id);
		if(sb!=null && hasOrgAuth(request, sb.getOrg().getId())){
		
			String code = sb.getCode();
			request.setAttribute("code", code);
			DataLog log = sbService.queryLastDataLog(code);
			MRecord record = MRecord.from(log);
			request.setAttribute("code", code);
			request.setAttribute("record",record);
			
//			从数据库查询
			Heart heart = sbService.queryLastHeart(code);
			request.setAttribute("heart", heart);
//			Client client = null;
//			try{ 
//				client = Client.getInstance();
//				if(client==null){
//					return "";
//				}
//				client.write("$GetHeartFromWeb,"+code+"!");
//				String result = client.read();
//			
//
//			}catch(Exception e){
//				e.printStackTrace();
//			
//			}finally{
//				Client.close(client);
//			}
		}
		
	
		request.setAttribute("id",id);
		return "platform/sbgl/mgr_sb";
	} 
	
	@RequestMapping("/viewDataLog.do")
	public String viewDataLog(HttpServletRequest request,Long id,String code,String beginTime, String endTime,String type,Page page){
		request.setAttribute("id",id);
		request.setAttribute("code", code);
		request.setAttribute("type", type);
		request.setAttribute("beginTime",beginTime);
		request.setAttribute("endTime", endTime);
		Timestamp begin=TimeUtil.parseTime(beginTime);
		Timestamp end=TimeUtil.parseTime(endTime);
		Page  returnPage = sbService.queryDataLogList(id,code,begin,end,page,type);
		List<?> list = returnPage.getPageList();
		List<MRecord> records = new ArrayList<MRecord>();
		if(list!=null){
			for (Object obj : list) {
				MRecord r = MRecord.from((DataLog)obj);
				if(r!=null){
					records.add(r);
				}
			}
		}
		returnPage.setPageList(records);
		int powDataSize = records.size()==0?1:records.get(0).getPowDataList().size();
		request.setAttribute("page", returnPage);
		request.setAttribute("powDataSize", powDataSize);
		return "platform/sbgl/view_data_log";
	}
	
	
	
	@RequestMapping("/viewLoginLog.do")
	public String viewLoginLog(HttpServletRequest request,Long id,String code,String beginTime, String endTime,Page page){
		request.setAttribute("id",id);
		request.setAttribute("code", code);
		request.setAttribute("beginTime",beginTime);
		request.setAttribute("endTime", endTime);
		Timestamp begin=TimeUtil.parseTime(beginTime);
		Timestamp end=TimeUtil.parseTime(endTime);
		Page  returnpPage = sbService.queryDataLogList(id,code,begin,end,page,"login");
		request.setAttribute("page", returnpPage);
		return "platform/sbgl/view_login_log";
	}
	
	@RequestMapping("/queryFrom.do")
	@ResponseBody
	public ResultData queryFrom(HttpServletRequest request,String code){
		Client client = null;
		try{ 
			client = Client.getInstance();
			if(client==null){
				return new ResultData(ResultData.ERROR,"无法连接到通讯服务器",null);
			}
			client.write("$QueryFromWeb,"+code+"!");
			String result = client.read();
			if("ok".equals(result)){
				return new ResultData(ResultData.SUCCESS,"已经发送查询命令，设备响应后会自动更新数据",null);
			}else{
				return new ResultData(ResultData.ERROR,"无法连接到设备",null);
			}

		}catch(Exception e){
			e.printStackTrace();
			return new ResultData(ResultData.ERROR,"从设备获取数据异常,"+e.getMessage(),null);
		}finally{
			Client.close(client);
		}
	}
	
	private  void queryFromNetty(final String code){
		
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					Client client = null;
					try{
						client = Client.getInstance();
						if(client==null){
							return ;
						}
						client.write("$QueryFromWeb,"+code+"!");
						client.read();
					}catch(Exception e){
						e.printStackTrace();
						return ;
					}finally{
						Client.close(client);
					}
					
				}
			});
			
			t.start();

	}
	

	@RequestMapping("/setSBParam.do")
	@ResponseBody
	public ResultData setSBParamToNetty(HttpServletRequest request,Long id,MPreData preData, MSetPowData setPowData,String turn){
		
		SB sb = sbService.getById(id);
		if(sb==null || !hasOrgAuth(request, sb.getOrg().getId())){
			return  new ResultData(ResultData.ERROR,"设备不存在或没有权限",ResultData.OPT_DELETE);
		}
		
		boolean isRootOrg = false;
		User loginUser = (User)request.getSession().getAttribute("loginUser");
		Organization org = organizationService.querySingleOrgaByUser(loginUser.getId());
		if(org!=null && "10".equals(org.getCode())){
			isRootOrg = true;
		}
		
		Client client = null;
		try {
			client = Client.getInstance();
			if(client==null){
				return new ResultData(ResultData.ERROR,"无法连接到通讯服务器",null);
			}
			StringBuffer sbf = new StringBuffer();
			sbf.append("$SetAllFromWeb,");
			sbf.append(sb.getCode()).append(",");
			sbf.append(preData.getPadValue()).append(",");//1衰减值
			sbf.append(setPowData.getFanTemp()).append(",");//2 风扇启动温度
			sbf.append(preData.getTotalRefLimit()).append(",");//3整机反射门限值
			sbf.append(preData.getTotalInLowerLimit()).append(",");//4整机输入下限
			sbf.append(preData.getTotalInUpperLimit()).append(",");//5 整机输入上限
		
			sbf.append(setPowData.getWarnTemp()).append(",");//6告警温度值
			sbf.append(setPowData.getWarnCurr()).append(",");//7告警电流
			sbf.append(setPowData.getWarnRef()).append(",");//8告警反射值
			sbf.append(turn+"").append(",");//9 TODO 开关机
			sbf.append(isRootOrg?"1":"0").append("!");//10 是否是超级用户
			
			client.write(sbf.toString());
		
			String result = client.read();
			if("ok".equals(result)){
				return new ResultData(ResultData.SUCCESS,"已经成功向设备发送命令",null);
			}else{
				return new ResultData(ResultData.ERROR,"无法连接到设备",null);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return new ResultData(ResultData.ERROR,"服务器错误,"+e.getMessage(),null);
		}finally{
			Client.close(client);
		}
		
		
		
	}
	
	
	@RequestMapping("/m_lastData.do")
	@ResponseBody
	public ResultData getMLastData(HttpServletRequest request,String code){
		
		Client client = null;
		try{ 
			client = Client.getInstance();
			if(client!=null){
				client.write("$QueryFromWeb,"+code+"!");
				String result = client.read();
				System.out.println("手机查询结果:"+result);
				Thread.sleep(3000);
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		 
		DataLog log = sbService.queryLastDataLog(code);
		MRecord record = MRecord.from(log);
		return new ResultData(ResultData.SUCCESS, "查询成功", null, record);
	}

	@ResponseBody
	@RequestMapping("/m_DeviceList.do")
	public ResultData getMSBListByLoginUser(HttpServletRequest request){
		MySession mySession = MySessionContext.getSession(request.getParameter("sessionId"));
		User user = (User)mySession.getAttribute("loginUser");
		Organization userOrg = organizationService.querySingleOrgaByUser(user.getId());
		List<Organization> orgList = organizationService.queryAllOrga(userOrg.getId());
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		long currTime = System.currentTimeMillis();
		for (Organization org : orgList) {
			List<SB> ll = sbService.getListByOrgId(org.getId());
			for (SB sb : ll) {
				
				DataLog log = sbService.queryLastDataLog(sb.getCode());
				Heart heart = sbService.queryLastHeart(sb.getCode());
				Timestamp lastTime = new Timestamp(0);
				if(log!=null){
					if(log.getTime().after(lastTime)){
						lastTime = log.getTime();
					}
				}
				if(heart!=null){
					if(heart.getTime().after(lastTime)){
						lastTime = heart.getTime();
					}
				}
				if(lastTime.getTime()!=0){
					sb.setLastTime(lastTime);
					if(currTime-lastTime.getTime()>300000){
						sb.setAlarm(true);
					}
				}else{
					sb.setAlarm(true);
				}
				
				Map<String,Object> map= new HashMap<String,Object>();
				list.add(map);
				map.put("id", sb.getId());
				map.put("code", sb.getCode());
				map.put("lastTime", sb.getLastTime());
				map.put("address", sb.getAddr());
				map.put("installTime", sb.getInstallTime().getTime());
				map.put("manager",sb.getMgrPersion());
				map.put("phone", sb.getPhone());
				
				
				
			}
		}
		return new ResultData(ResultData.SUCCESS,null,null,list);
	}
		
}
