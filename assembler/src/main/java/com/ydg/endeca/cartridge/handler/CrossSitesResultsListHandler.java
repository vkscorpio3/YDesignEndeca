package com.ydg.endeca.cartridge.handler;

import java.util.ArrayList;
import java.util.List;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.ResultsList;
import com.endeca.infront.cartridge.ResultsListConfig;
import com.endeca.infront.cartridge.ResultsListHandler;
import com.endeca.infront.cartridge.model.NavigationAction;
import com.endeca.infront.navigation.NavigationState;
import com.endeca.infront.navigation.model.FilterState;
import com.ydg.endeca.site.Site;
import com.ydg.endeca.site.SiteDirectory;

import org.apache.log4j.Logger;

/**
 * The Class CrossSitesResultsListHandler.
 */
public class CrossSitesResultsListHandler extends ResultsListHandler{
	
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(CrossSitesResultsListHandler.class);
	public static final String PROP_STORE_ID="Store_id";
	public static final String PROP_NAME_CROSS_SITE_STORE_ID = "crossSiteID";
	private SiteDirectory siteDirectory;

	/* 
	 * Takes the current navigation state, and does the following to it:
	 * 		Replace the current store ID, with the cross site id
	 * 		Remove any refinements from the navigation state since we only want to perform the search for 
	 * 			the other site we do not want to carry over any selected refinements.
	 * 
	 * (non-Javadoc)
	 * @see com.endeca.infront.cartridge.ResultsListHandler#preprocess(com.endeca.infront.cartridge.ResultsListConfig)
	 */
	@Override
	public void preprocess(ResultsListConfig config)
			throws CartridgeHandlerException {
		
		//only do something if there's a search term
		NavigationState navState = this.getNavigationState();		
		if(navState.getFilterState().getSearchFilters()==null || navState.getFilterState().getSearchFilters().isEmpty()){
			return;
		}
		
		//replace the site id in the record filters
        navState = replaceSiteIDInRecordFilters(navState, config);

        //remove any refinements
        navState = navState.clearNavigationFilters();
        
        //now set the modified navigation state to this handler instance        
        this.setNavigationState(navState);
        
        //we really don't want to inherit the current page's pagination, so we'll set it to start on 0th page.
        config.setOffset(0);
        
		super.preprocess(config);
		if(logger.isDebugEnabled()){
			logger.debug("inside preprocess()");
		}
	}


	/**
	 * Replace site id in record filters.
	 *
	 * @param navState the nav state
	 * @param config the config
	 * @return the navigation state
	 */
	private NavigationState replaceSiteIDInRecordFilters(
			NavigationState navState, ResultsListConfig config) {
		FilterState filterStateCopy = navState.getFilterState().clone();
        List<String> recordFiltersCopy = new ArrayList<String>(filterStateCopy.getRecordFilters());
        List<String> newRecordFilters = new ArrayList<String>();
        for(String filter: recordFiltersCopy){        	
        	if(isSiteFilter(filter)){
        		newRecordFilters.add(PROP_STORE_ID + ":" + config.get(PROP_NAME_CROSS_SITE_STORE_ID));
        	}
        	else{
        		newRecordFilters.add(filter);
        	}
        }
        filterStateCopy.setRecordFilters(newRecordFilters);
        return navState.updateFilterState(filterStateCopy);
	}


	/**
	 * Checks if the filter is a site filter.
	 *
	 * @param filter the filter
	 * @return true, if is site filter
	 */
	private boolean isSiteFilter(String filter) {
		if(filter.contains(PROP_STORE_ID)){
			return true;
		}
		return false;
	}


	@Override
	public ResultsList process(ResultsListConfig cartridgeConfig)
			throws CartridgeHandlerException {
		

		//only do something if there's a search term
		NavigationState navState = this.getNavigationState();		
		if(navState.getFilterState().getSearchFilters()==null || navState.getFilterState().getSearchFilters().isEmpty()){
			return null;
		}
		
		
		if(logger.isDebugEnabled()){
			logger.debug("inside process(), returning null for now");
		}
		ResultsList ret = new ResultsList(cartridgeConfig);
		ret.put("handler", this);
		ret.put("config", cartridgeConfig);
		return ret;
	}
	
	public ResultsList process2(ResultsListConfig cartridgeConfig)
			throws CartridgeHandlerException {
		if(logger.isDebugEnabled()){
			logger.debug("inside process2()");
		}
		ResultsList ret = super.process(cartridgeConfig);
		cleanupResults(ret, cartridgeConfig);
		return ret;
	}
	

    /**
     * Cleanup results content item before returning.  
     * 	Removes unnecessary properties from original results list base class, that we're not interested in.
     *	Adds site details based on the siteId that was configured in the cartridge config
     * @param cartridgeConfig 
     * @param ret the ret
     */
    private void cleanupResults(ResultsList results, ResultsListConfig cartridgeConfig) {
		if(results!=null){
			results.setSortOptions(null);
			results.put("firstRecNum",null);
			results.put("lastRecNum",null);
			results.setPagingActionTemplate(null);
			results.setPrecomputedSorts(null);
			results.put(PROP_NAME_CROSS_SITE_STORE_ID, null);
			
			
			
			Site site = this.getSiteDirectory().getMapById().get(cartridgeConfig.get(PROP_NAME_CROSS_SITE_STORE_ID));
			if(site!=null){
				NavigationAction link = new NavigationAction(this.getNavigationState().toString(), "Search");
				link.setContentPath( "/" + site.getContextPath());
				link.setSiteRootPath(getActionPathProvider().getDefaultNavigationActionSiteRootPath());
				results.put("link", link);

				results.put("Site", site.getName());
				results.put("SiteID",site.getId());
			}
		}
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
