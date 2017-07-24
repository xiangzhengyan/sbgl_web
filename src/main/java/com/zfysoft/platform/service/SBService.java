package com.zfysoft.platform.service;

import java.sql.Timestamp;
import java.util.List;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.model.sb.DataLog;
import com.zfysoft.platform.model.sb.Heart;
import com.zfysoft.platform.model.sb.SB;

/**
 * @author xiangzy
 * @date 2015-9-9
 * 
 */
public interface SBService {
	public void save(SB sb);

	public List<SB> getListByOrgId(Long orgId);

	public SB getById(Long id);
	
	public void delete(SB id);


	public DataLog queryLastDataLog(String  code);
	
	public Heart queryLastHeart(String code);


	public Page queryDataLogList(Long id, String code, Timestamp begin,
			Timestamp end, Page page, String type);

}
