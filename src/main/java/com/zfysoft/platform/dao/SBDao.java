package com.zfysoft.platform.dao;

import java.sql.Timestamp;
import java.util.List;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.model.sb.DataLog;
import com.zfysoft.platform.model.sb.Heart;
import com.zfysoft.platform.model.sb.SB;

/**
 * @author xiangzy
 * @date 2015-9-8
 * 
 */
public interface SBDao {
	public void save(SB sb);
	public List<SB> getListByOrgId(Long orgId);
	public SB getById(Long id);
	/**
	 * @param id
	 */
	public void delete(SB sb);

	 

	public DataLog queryLastDataLog(String code);
	
	public int queryDataLogListCount(Long id, String code, Timestamp begin,
			Timestamp end, String type);

	public List<DataLog> queryDataLogList(Long id, String code, Timestamp begin,
			Timestamp end, Page page, String type);
	/**
	 * @param code
	 * @return
	 */
	public Heart queryLastHeart(String code);

	
}
