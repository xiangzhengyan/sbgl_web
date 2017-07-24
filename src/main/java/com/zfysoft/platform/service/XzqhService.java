package com.zfysoft.platform.service;

import java.util.List;

import com.zfysoft.platform.model.Xzqh;

public interface XzqhService {
	
	/**
	 * 查询所有的行政区划
	 */
	public List<Xzqh> getAllXzqh();
	
	/**
	 * 插入行政区划(初始导入用)
	 */
	public void saveXzqhList(List<Xzqh> list);

	/**
	 * @param xzqh
	 */
	void saveXzqh(Xzqh xzqh);

}
