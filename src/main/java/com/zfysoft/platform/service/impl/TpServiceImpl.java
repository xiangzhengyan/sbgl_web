package com.zfysoft.platform.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zfysoft.platform.dao.TpDao;
import com.zfysoft.platform.model.UploadImages;
import com.zfysoft.platform.service.TpService;

/**
 * 图片Service
 */
@Service("tpService")
public class TpServiceImpl implements TpService{
	
	@Resource
	private TpDao tpDao;

	@Override
	public UploadImages getById(Long id) {
		return tpDao.getById(UploadImages.class, id);
	}
	

}
