package com.zfysoft.platform.listener;

import javax.servlet.http.HttpSession;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import com.zfysoft.platform.model.RevInfo;
import com.zfysoft.platform.model.User;

/**
 * @author xiangzy
 * @date 2015-8-6
 *
 */
public class RevisionListener implements org.hibernate.envers.RevisionListener{

	@Override
	public void newRevision(Object obj) {
		RevInfo revInfo = (RevInfo) obj;
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        User user = (User)session.getAttribute("loginUser");
        if(user!=null){
        	revInfo.setUserId(user.getLoginName());
        	revInfo.setIp((String)session.getAttribute("ip"));
        }
		
	}



}
