package com.ydg.endeca.cartridge.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.assembler.ContentItem;
import com.endeca.infront.cartridge.ResultsList;
import com.endeca.infront.cartridge.ResultsListConfig;
import com.endeca.infront.cartridge.ResultsListHandler;
import com.endeca.infront.cartridge.model.SortOptionConfig;
import com.ydg.endeca.utils.CartridgeShare;

/**
 * The Class CrossSitesResultsListHandler.
 */
public class YDGResultsListHandler extends ResultsListHandler{

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(YDGResultsListHandler.class);
	private static final String PROP_NAME_CROSS_SITE_RESULTSLIST = "crossSiteResults";
	private static final String BESTSELLER_PLACEHOLDER_DESCENDING="placeholder|1";
	private static final String BESTSELLER_PLACEHOLDER_ASCENDING="placeholder|0";
	private static final String PROP_NAME_BESTSELLER_SORT="bestSellerSort";
	private static final String FLAG_DESCENDING="|1";
	private static final String FLAG_ASCENDING="|0";
	private CartridgeShare cartridgeShare;
	
	@Override
	public void preprocess(ResultsListConfig config)
			throws CartridgeHandlerException {

		/*since there are 3 properties of best seller, one for each site
		we have to set the right best seller sort key based on what site
		they selected in the cartridge configuration.*/
		setBestSellerSort(config);

		super.preprocess(config);

		if(logger.isDebugEnabled()){
			logger.debug("inside preprocess()");
		}
	}

	/**
	 * Sets the best seller sort based on selection in the cartridge configuration.
	 * @param config 
	 * @param config 
	 */
	private void setBestSellerSort(ResultsListConfig config) {
		for(SortOptionConfig sortOptionConfig :  this.getSortOptions()){
			String value = sortOptionConfig.getValue();
			if(BESTSELLER_PLACEHOLDER_DESCENDING.equals(value)){
				sortOptionConfig.setValue((String)config.get(PROP_NAME_BESTSELLER_SORT) + FLAG_DESCENDING);
			}
			else if (BESTSELLER_PLACEHOLDER_ASCENDING.equals(value)){
				sortOptionConfig.setValue((String)config.get(PROP_NAME_BESTSELLER_SORT) + FLAG_ASCENDING);				
			}
		}		
	}

	/* (non-Javadoc)
	 * @see com.endeca.infront.cartridge.ResultsListHandler#process(com.endeca.infront.cartridge.ResultsListConfig)
	 */
	@Override
	public ResultsList process(ResultsListConfig cartridgeConfig)
			throws CartridgeHandlerException {

		if(logger.isDebugEnabled()){
			logger.debug("inside process()");
		}
		ResultsList ret = super.process(cartridgeConfig);
		
		
		//set the best seller sort to the right key/name depending on the site selection in the cartridge configuration
				
		if(ret!=null){
			/*
			 * If this is the last page of the results list, then we need to display the child Cross Site Results List.
			 *  At this point of time they haven't really executed their queries yet.  The following code
			 *  will iterate through these children, and call process so that they will execute their MDEX queries.
			 */

			boolean lastPage = (ret.getLastRecNum()==ret.getTotalNumRecs());			
			if(lastPage){
				//this would contain the results of the child cross site result cartridges
				List<ContentItem>newCrossSiteResults = new ArrayList<ContentItem>();

				//iterate through the child cross site results lists, and for each one
				//call the process2() method of them, and store the results returned by process2
				//to newCrossSiteResults
				List<ContentItem>crossSiteResults = (List<ContentItem>)ret.get(PROP_NAME_CROSS_SITE_RESULTSLIST);
				if(crossSiteResults!=null && !crossSiteResults.isEmpty()){
					for(ContentItem crossSiteResultsList: crossSiteResults){
						newCrossSiteResults.add(processCrossSite(crossSiteResultsList));
					}
					
					//we'll now store the results of the cross site results list to the "crossSiteResults" property of this parent cartridge.
					ret.put(PROP_NAME_CROSS_SITE_RESULTSLIST, newCrossSiteResults);
				}
			}
			else{
				//we do not need to show the cross site results since this is not the last page yet.
				//just clear out the configuration for them
				
				if(logger.isDebugEnabled()){
					logger.debug("No need for cross site results since it's not the last page yet");					
				}
				
				ret.put("crossSiteResults", null);
			}
		}
		getCartridgeShare().put("resultsList", ret);
		return ret;
	}

	
	public CartridgeShare getCartridgeShare() {
		return cartridgeShare;
	}

	public void setCartridgeShare(CartridgeShare cartridgeShare) {
		this.cartridgeShare = cartridgeShare;
	}

	private ResultsList processCrossSite(ContentItem crossSiteResultsList ) {
		if(crossSiteResultsList==null){
			return null;
		}

		CrossSitesResultsListHandler handler = (CrossSitesResultsListHandler)crossSiteResultsList.get("handler");
		ResultsListConfig config = (ResultsListConfig)crossSiteResultsList.get("config");
		
		
		try {
			return handler.process2(config);
		} catch (CartridgeHandlerException e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	/**
	 * Determine if this is last page.
	 *
	 * @param ret the ret
	 * @return true, if successful
	 */
	private boolean determineIfThisIsLastPage(ResultsList ret) {
		if(ret==null){
			//this is unexpected, oh well, lets treat it as last page also
			return true;
		}

		long lastRecordNum = ret.getLastRecNum();
		long numResults = ret.getTotalNumRecs();
		if(lastRecordNum==numResults){
			return true;
		}

		return false;
	}
}
