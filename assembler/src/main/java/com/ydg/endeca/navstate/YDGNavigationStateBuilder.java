package com.ydg.endeca.navstate;

import java.util.List;

import org.apache.log4j.Logger;

import com.endeca.infront.navigation.NavigationException;
import com.endeca.infront.navigation.NavigationState;
import com.endeca.infront.navigation.model.FilterState;
import com.endeca.infront.navigation.url.UrlNavigationStateBuilder;
import com.endeca.soleng.urlformatter.UrlState;
import com.ydg.endeca.site.Site;
import com.ydg.endeca.site.SiteDirectory;
public class YDGNavigationStateBuilder extends UrlNavigationStateBuilder{
	



	private static final String PATH_DELIMITER = "/";
	private static final int SITE_ID_TOKEN_LOCATION = 1;
	private SiteDirectory siteDirectory;
	private static final Logger logger = Logger.getLogger(YDGNavigationStateBuilder.class);

    
    
	@Override
	public NavigationState parseNavigationState(String queryString,
			String pathInfo, String characterEncoding) throws NavigationException {
		NavigationState ret = super.parseNavigationState(queryString, pathInfo, characterEncoding);
		ret = addSiteFilters(ret, pathInfo);
		return ret;
	}

	private NavigationState addSiteFilters(NavigationState navState, String pathInfo) {
		if(pathInfo==null || navState ==null){
			return navState;
		}
		String tokens[] = pathInfo.split(PATH_DELIMITER);
		if(tokens!=null && tokens.length>=SITE_ID_TOKEN_LOCATION+1){
			String contextPath = tokens[SITE_ID_TOKEN_LOCATION];
			if(contextPath!=null){
				Site site = this.getSiteDirectory().getMapByContextPath().get(contextPath);
				if(site!=null){
					String siteId = site.getId();
					List<String> recordFilters = navState.getFilterState().getRecordFilters();
					recordFilters.add("Store_id:" + siteId);
					if(logger.isDebugEnabled()){
						logger.debug("Adding site record filter: " + "Store_id:" + siteId);
					}
					FilterState filterStateCopy = navState.getFilterState().clone();
					filterStateCopy.setRecordFilters(recordFilters);
					return navState.updateFilterState(filterStateCopy);
				}
			} 
		}
		return navState;
	}	 
	
	
    /**
     * Gets the site directory.
     *
     * @return the site directory
     */
    public SiteDirectory getSiteDirectory() {
		return siteDirectory;
	}

	/**
	 * Sets the site directory.
	 *
	 * @param siteDirectory the new site directory
	 */
	public void setSiteDirectory(SiteDirectory siteDirectory) {
		this.siteDirectory = siteDirectory;
	}
}
