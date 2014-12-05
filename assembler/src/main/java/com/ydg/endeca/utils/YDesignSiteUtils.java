package com.ydg.endeca.utils;

import java.util.HashMap;
import java.util.Map;

public class YDesignSiteUtils {

    public static final String PROP_STORE_ID="Store_id";
	private static final Map<String, String> siteMap;
	static
	    {
	    	siteMap = new HashMap<String, String>();
	    	siteMap.put( "yl", "ylighting");
	    	siteMap.put("yb" , "ybath");
	    	siteMap.put( "yv", "yliving");
	    }

	 /**
	 * Checks if the filter is a site filter.
	 *
	 * @param filter the filter
	 * @return true, if is site filter
	 */
	public static boolean isSiteFilter(String filter) {
		if(filter.contains(PROP_STORE_ID)){
			return true;
		}
		return false;
	}

	public static String getContextBySiteId(String siteId) {
		return siteMap.get(siteId);
	}
}
