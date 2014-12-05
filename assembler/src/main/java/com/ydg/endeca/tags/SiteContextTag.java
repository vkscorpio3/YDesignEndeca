package com.ydg.endeca.tags;

import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.endeca.infront.navigation.NavigationState;
import com.ydg.endeca.utils.YDesignSiteUtils;

public class SiteContextTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	private String var;

	    public String getVar() {
	        return var;
	    }

	    public void setVar(String var) {
	        this.var = var;
	    }
	    
	      
	    @Override
	    public int doStartTag() throws JspException {
	        try {
	        	 WebApplicationContext webappCtx = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
	          	if(webappCtx!=null){
	          		//find the bean named environmentProperties
	          		NavigationState navState = (NavigationState) webappCtx.getBean("navigationState");
	          		if(navState!=null){
	          			List<String> recordFilters = navState.getFilterState().getRecordFilters();
	          			String siteFilter=null;
	          			for(String filter: recordFilters){
	          	        	if(YDesignSiteUtils.isSiteFilter(filter)){
	          	        		siteFilter = filter;
	          	        		break;
	          	        	}	          			
	          	        }
	          			
	          			if(siteFilter!=null){
	          				String[]fields = siteFilter.split(":");
	          				if(fields.length>=2){
	          					String siteId = fields[1];
	          					if(YDesignSiteUtils.getContextBySiteId(siteId)!=null){
	          			            pageContext.setAttribute(var,YDesignSiteUtils.getContextBySiteId(siteId));
	          			            return TagSupport.SKIP_BODY;
	          					}
	          				}
	          			}
	          		}
	          	}
	            pageContext.setAttribute(var,"");
	        } catch (Exception e) {
	            throw new JspException(e);
	        }
	        return TagSupport.SKIP_BODY;

	    }
	    
}
