package com.zfysoft.platform.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zfysoft.platform.cache.CommonCDCache;

/**
 * 字典（自定义标签）
 * @author Administrator
 *
 */
public class ZdTag extends TagSupport {

	private static final long serialVersionUID = -7096123302735954605L;
	private String code;
	
	private String group;
	
	@Override
    public int doEndTag() throws JspException {
        try {
        	 this.pageContext.getOut().write(CommonCDCache.getLabel(group, code));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

	
	public String getGroup() {
		return group;
	}


	public void setGroup(String group) {
		this.group = group;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}

