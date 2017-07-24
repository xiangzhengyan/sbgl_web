package com.zfysoft.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zfysoft.platform.dao.XzqhDao;
import com.zfysoft.platform.model.Xzqh;
import com.zfysoft.platform.service.XzqhService;
@Service("xzqhService")
public class XzqhServiceImpl implements XzqhService {
	
	@Resource
	private XzqhDao xzqhDao;

	@Override
	public List<Xzqh> getAllXzqh() {
		return xzqhDao.getAllXzqh();
	}

	@Override
	public void saveXzqhList(List<Xzqh> list) {
		xzqhDao.saveXzqhList(list);

	}
	
	@Override
	public void saveXzqh(Xzqh xzqh) {
		xzqhDao.saveXzqh(xzqh);

	}

}
