package com.zfysoft.platform.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.stereotype.Component;

import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.cache.XzqhCache;
@Component
public class DwdmTag extends TagSupport {
	
	private static final long serialVersionUID = -5345930338683389353L;

	private String dwdm;
	
	@Override
    public int doEndTag() throws JspException {
        try {
    		if (dwdm.indexOf(",") > 0) {
    			String name = "";
    			String[] arrayStr =new String[]{};
    		    arrayStr = dwdm.split(",");
    		    List<String> list = java.util.Arrays.asList(arrayStr);
    		    for (String dwdm : list) {
    		    	name = name + XzqhCache.getByDwdm(dwdm)+ ",";
    		    }
    		    
    		    this.pageContext.getOut().write(name.substring(0, name.length() - 1));
    		} else {
    			this.pageContext.getOut().write(StringUtil.isEmptyOrNull(dwdm)?"":XzqhCache.getCompleteMc(dwdm));
    		}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

	public String getDwdm() {
		return dwdm;
	}

	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}


}
