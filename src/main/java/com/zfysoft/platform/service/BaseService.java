/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.service;

import java.util.List;


public interface BaseService {
	/**
	 * 保存多个数据对象
	 * @param list
	 * @return
	 */
	public <T> Boolean saveList(List<T> list);
}
