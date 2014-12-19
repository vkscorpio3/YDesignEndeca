package com.ydg.endeca.action;

import org.apache.log4j.Logger;

import com.endeca.infront.navigation.url.ActionPathProvider;


public class TypeAheadActionPathProvider implements ActionPathProvider{
	private static final Logger logger = Logger.getLogger(TypeAheadActionPathProvider.class);
	private String navigationSiteRootPath="";
	private String navigationActionPath="";
	private String recordSiteRootPath="";
	private String recordActionPath="";

	
	@Override
	public String getDefaultNavigationActionSiteRootPath() {
		return getNavigationSiteRootPath();
	}

	@Override
	public String getDefaultNavigationActionContentPath() {
		if(logger.isDebugEnabled()){
			logger.debug("returning " + getNavigationActionPath());
		}
		return getNavigationActionPath();
	}

	@Override
	public String getDefaultRecordActionSiteRootPath() {
		return navigationSiteRootPath;

	}

	@Override
	public String getDefaultRecordActionContentPath() {
		return getRecordActionPath();
	}


	public String getNavigationSiteRootPath() {
		return getRecordSiteRootPath();
	}

	public void setNavigationSiteRootPath(String navigationSiteRootPath) {
		this.navigationSiteRootPath = navigationSiteRootPath;
	}

	public String getNavigationActionPath() {
		return navigationActionPath;
	}

	public void setNavigationActionPath(String navigationActionPath) {
		this.navigationActionPath = navigationActionPath;
	}

	public String getRecordSiteRootPath() {
		return recordSiteRootPath;
	}

	public void setRecordSiteRootPath(String recordSiteRootPath) {
		this.recordSiteRootPath = recordSiteRootPath;
	}

	public String getRecordActionPath() {
		return recordActionPath;
	}

	public void setRecordActionPath(String recordActionPath) {
		this.recordActionPath = recordActionPath;
	}
}
