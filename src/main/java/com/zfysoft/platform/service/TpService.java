package com.zfysoft.platform.service;

import com.zfysoft.platform.model.UploadImages;


public interface TpService {
	
	/**
	 * 根据id获取图片
	 */
	public UploadImages getById(Long id);
}
