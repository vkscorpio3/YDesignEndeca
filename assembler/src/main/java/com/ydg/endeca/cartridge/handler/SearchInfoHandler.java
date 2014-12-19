package com.ydg.endeca.cartridge.handler;

import com.endeca.infront.assembler.BasicContentItem;
import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.assembler.ContentItem;
import com.endeca.infront.cartridge.Breadcrumbs;
import com.endeca.infront.cartridge.NavigationCartridgeHandler;
import com.endeca.infront.cartridge.ResultsList;
import com.ydg.endeca.utils.CartridgeShare;
import com.ydg.endeca.utils.PostprocessCartridgeHandler;

/**
 * The Class CrossSitesResultsListHandler.
 */
public class SearchInfoHandler extends NavigationCartridgeHandler<BasicContentItem, BasicContentItem>
	implements PostprocessCartridgeHandler{
	private CartridgeShare cartridgeShare;
	
	private static final String FIRST_REC_NUM = "firstRecNum";
	private static final String LAST_REC_NUM = "lastRecNum";
	private static final String TOTAL_NUM_RECS = "totalNumRecs";
	private static final String SEARCH_CRUMBS = "searchCrumbs";

	@Override
	protected BasicContentItem wrapConfig(ContentItem paramContentItem) {
		return new BasicContentItem(paramContentItem);
	}

	/* (non-Javadoc)
	 * @see com.endeca.infront.cartridge.NavigationCartridgeHandler#process(com.endeca.infront.assembler.ContentItem)
	 */
	@Override
	public BasicContentItem process(BasicContentItem pContentItem)
			throws CartridgeHandlerException { 
		CartridgeShare share = getCartridgeShare();

		//return an empty result that we will later populate during the postProcess method
		BasicContentItem ret = new BasicContentItem("SearchInfo");
		
		/*put into share so that postProcess has access to it*/
		share.put("searchinfo",ret);
		
		
		/*Register self to postProcess cartridges so that our postProcess method will get executed later*/
		share.getPostProcessCartridges().add(this);		

		//return an empty result that we will later populate during the postProcess method
		return ret;
	}
 
	/* 
	 * Try to find the results list in the cartridgeShare, and copy some items of interest into 
	 * the search info object.
	 */
	@Override
	public void postProcess() {
		ResultsList results = (ResultsList) getCartridgeShare().get("resultsList");
		BasicContentItem searchInfo = (BasicContentItem) getCartridgeShare().get("searchinfo");

		//copy items from results list into searchInfo
		if(results!=null && searchInfo!=null){
			searchInfo.put(TOTAL_NUM_RECS, results.get(TOTAL_NUM_RECS));
			searchInfo.put(FIRST_REC_NUM, results.get(FIRST_REC_NUM));			
			searchInfo.put(LAST_REC_NUM, results.get(LAST_REC_NUM));
		}
		
		Breadcrumbs breadcrumbs = (Breadcrumbs) getCartridgeShare().get("breadcrumb");
		//copy items from breadcrumbs into searchInfo		
		if(searchInfo!=null &&  breadcrumbs!=null){
			searchInfo.put(SEARCH_CRUMBS, breadcrumbs.get(SEARCH_CRUMBS));
		}
		
	}
	
	

	public CartridgeShare getCartridgeShare() {
		return cartridgeShare;
	}

	public void setCartridgeShare(CartridgeShare cartridgeShare) {
		this.cartridgeShare = cartridgeShare;
	}


	
}
