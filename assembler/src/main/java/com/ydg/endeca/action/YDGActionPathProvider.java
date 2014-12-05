package com.ydg.endeca.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.endeca.infront.content.ContentException;
import com.endeca.infront.content.source.ContentSource;
import com.endeca.infront.refapp.navigation.BasicActionPathProvider;
import com.endeca.infront.site.model.SiteState;


public class YDGActionPathProvider extends BasicActionPathProvider{
	private static final Logger logger = Logger.getLogger(YDGActionPathProvider.class);

	@Override
	public String getDefaultNavigationActionSiteRootPath() {
		
		if(logger.isDebugEnabled()){
			logger.debug("inside getDefaultNavigationActionSiteRootPath()");
		}
		
		String ret = super.getDefaultNavigationActionSiteRootPath();
		

		if(logger.isDebugEnabled()){
			logger.debug("returning " + ret);
		}
		
		return ret;
	}

	@Override
	public String getDefaultNavigationActionContentPath() {

		if(logger.isDebugEnabled()){
			logger.debug("inside getDefaultNavigationActionContentPath()");
		}
		
		String ret = super.getDefaultNavigationActionContentPath();
		

		if(logger.isDebugEnabled()){
			logger.debug("returning " + ret);
		}
		
		return ret;
	}

	@Override
	public String getDefaultRecordActionSiteRootPath() {

		if(logger.isDebugEnabled()){
			logger.debug("inside getDefaultRecordActionSiteRootPath()");
		}
		
		String ret = super.getDefaultRecordActionSiteRootPath();
		

		if(logger.isDebugEnabled()){
			logger.debug("returning " + ret);
		}
		
		return ret;
	}

	@Override
	public String getDefaultRecordActionContentPath() {

		if(logger.isDebugEnabled()){
			logger.debug("inside getDefaultRecordActionContentPath()");
		}
		
		String ret = super.getDefaultRecordActionContentPath();
		

		if(logger.isDebugEnabled()){
			logger.debug("returning " + ret);
		}
		
		return ret;
	}

	public YDGActionPathProvider(ContentSource source,
			HttpServletRequest request,
			Map<String, String> navigationActionUriMap,
			Map<String, String> recordActionUriMap) throws ContentException {
		super(source, request, navigationActionUriMap, recordActionUriMap);
		
		if(logger.isDebugEnabled()){
			logger.debug("Constructor called");
		}
	}

	public YDGActionPathProvider(ContentSource source,
			HttpServletRequest request,
			Map<String, String> navigationActionUriMap,
			Map<String, String> recordActionUriMap, 
			SiteState siteState)
			throws ContentException {
		

		super(source, request, navigationActionUriMap, recordActionUriMap, siteState);
		if(logger.isDebugEnabled()){
			logger.debug("Requst.getPathInfo "+ request.getPathInfo());
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("Constructor called");
		}
	}
	
}
