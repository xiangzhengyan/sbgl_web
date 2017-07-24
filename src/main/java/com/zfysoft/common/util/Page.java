package com.zfysoft.common.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

public class Page {
	
	private int pageSize = 25;
	private int pageNum = 1;
	private int totalPage = 0;
	private int prePage = 1;
	private int nextPage = 1;
	private int totalRows = 0;
	/**
	 * 字段（数据库）
	 */
	private String sortCol;
	/**
	 * 排序（desc、asc）
	 */
	private String sortOrder;
	private List<?> pageList;
	
	public Page(){}
	
	public Page(Integer pageSize){
		this.pageSize = pageSize;
	}
	
	public Query sqlStart(String sql,Query query,Session session){
		Query cQuery = session.createSQLQuery("select count(*) from ("+sql+")");
		totalRows = Integer.valueOf(cQuery.uniqueResult().toString()).intValue();
		totalPage = (totalRows  +  pageSize  - 1) / pageSize; 
		query.setFirstResult((pageNum-1)*pageSize);
		query.setMaxResults(pageSize);
		setPreNext();
		return query;
	}
	
	@SuppressWarnings("rawtypes")
	public Query sqlStart(String sql,Query query,Session session,Map<String,Object> params){
		Query cQuery = session.createSQLQuery("select count(*) from ("+sql+")");
		if(params != null) {
			for(String key : params.keySet()){
				if(params.get(key) instanceof List){
					cQuery.setParameterList(key, (List)params.get(key));
				}else if(params.get(key) instanceof Long){
					cQuery.setLong(key, (Long)params.get(key));
				}else if(params.get(key) instanceof Integer){
					cQuery.setInteger(key, (Integer)params.get(key));
				}else if(params.get(key) instanceof String){
					cQuery.setString(key, (String)params.get(key));
				}else if(params.get(key) instanceof Date){
					cQuery.setDate(key, (Date)params.get(key));
				}else if(params.get(key) instanceof Double){
					cQuery.setDouble(key, (Double)params.get(key));
				}else{
					cQuery.setParameter(key, params.get(key));
				}
			}
		}
		totalRows = Integer.valueOf(cQuery.uniqueResult().toString()).intValue();
		totalPage = (totalRows  +  pageSize  - 1) / pageSize; 
		query.setFirstResult((pageNum-1)*pageSize);
		query.setMaxResults(pageSize);
		setPreNext();
		return query;
	}
	
	public Query sqlStart(String sql,Query query,Session session,List<Object> params){
		Query cQuery = session.createSQLQuery("select count(*) from ("+sql+")");
		if(params != null) {
			int len = params.size();
			for(int i=0;i<len;i++){
				Object key = params.get(i);
				if(key instanceof Long){
					cQuery.setLong(i, (Long)key);
				}else if(key instanceof Integer){
					cQuery.setInteger(i, (Integer)key);
				}else if(key instanceof String){
					cQuery.setString(i, (String)key);
				}else if(key instanceof Date){
					cQuery.setDate(i, (Date)key);
				}else if(key instanceof Double){
					cQuery.setDouble(i, (Double)key);
				}else{
					cQuery.setParameter(i,key);
				}
			}
		}
		totalRows = Integer.valueOf(cQuery.uniqueResult().toString()).intValue();
		totalPage = (totalRows  +  pageSize  - 1) / pageSize; 
		query.setFirstResult((pageNum-1)*pageSize);
		query.setMaxResults(pageSize);
		setPreNext();
		return query;
	}
	
	public void doPage(){
		totalPage = (totalRows  +  pageSize  - 1) / pageSize;
		setPreNext();
	}

	public Query setPageConfig(Query query){
		totalRows = query.list().size();
		totalPage = (totalRows  +  pageSize  - 1) / pageSize; 
		query.setFirstResult((pageNum-1)*pageSize);
		query.setMaxResults(pageSize);
		setPreNext();
		return query;
	}
	
	public void setPreNext(){
		prePage = (pageNum-1) > 0 ? (pageNum-1) : 1;
		nextPage = (pageNum+1) <= totalPage ? (pageNum+1) : totalPage;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getTotalPage() {
		totalPage = this.totalRows % this.pageSize == 0 
				? this.totalRows / this.pageSize 
						: this.totalRows / this.pageSize + 1; 
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getPrePage() {
		return prePage;
	}
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
		totalPage = (totalRows  +  pageSize  - 1) / pageSize;
		setPreNext();
	}
	
	public List<?> getPageList() {
		return pageList;
	}

	public void setPageList(List<?> pageList) {
		this.pageList = pageList;
	}

	public String getSortCol() {
		return sortCol;
	}

	public void setSortCol(String sortCol) {
		this.sortCol = sortCol;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
}
