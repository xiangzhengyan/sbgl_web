package com.zfysoft.platform.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.dao.OrganizationDao;
import com.zfysoft.platform.dao.SBDao;
import com.zfysoft.platform.model.sb.DataLog;
import com.zfysoft.platform.model.sb.Heart;
import com.zfysoft.platform.model.sb.SB;
import com.zfysoft.platform.service.SBService;

/**
 * @author xiangzy
 * @date 2015-9-9
 *
 */
@Service("sbService")
public class SBServiceImpl implements SBService{

	@Resource
	private SBDao sbDao;
	
	@Resource
	private OrganizationDao organizationDao;
	
	@Override
	public void save(SB sb) {
		sbDao.save(sb);
		
	}


	@Override
	public List<SB> getListByOrgId(Long orgId) {
		return sbDao.getListByOrgId(orgId);
	}


	@Override
	public SB getById(Long id) {
		return sbDao.getById(id);
	}


	@Override
	public void delete(SB sb) {
		sbDao.delete(sb);
		
	}




	
	@Override
	public DataLog queryLastDataLog(String code) {
		return sbDao.queryLastDataLog(code);
	}



	@Override
	public Page queryDataLogList(Long id, String code, Timestamp begin,
			Timestamp end, Page page, String type) {
		int total = sbDao.queryDataLogListCount(id,code,begin,end,type);
		if(total>0){
			
			List<DataLog> list = sbDao.queryDataLogList(id,code,begin,end,page,type);
			page.setTotalRows(total);
			page.setPageList(list);
		}
		return page;
	}



	@Override
	public Heart queryLastHeart(String code) {
		return sbDao.queryLastHeart(code);
	}

}
