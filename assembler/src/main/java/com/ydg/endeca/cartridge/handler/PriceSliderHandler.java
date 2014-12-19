package com.ydg.endeca.cartridge.handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.Breadcrumbs;
import com.endeca.infront.cartridge.BreadcrumbsConfig;
import com.endeca.infront.cartridge.BreadcrumbsHandler;
import com.endeca.infront.navigation.NavigationState;
import com.ydg.endeca.query.PriceSliderQueryModifier;
import com.ydg.endeca.query.YDesignMdexResource;
import com.ydg.endeca.utils.SiteRecordFilterParser;

public class PriceSliderHandler extends BreadcrumbsHandler{
	private static final Logger logger = Logger.getLogger(PriceSliderHandler.class);
	private PriceSliderQueryModifier queryModifier  = null;
	private boolean sliderWasUsed=false;
	private HttpServletRequest httpServletRequest;
	
	@Override
	public void preprocess(BreadcrumbsConfig cartridgeConfig)
			throws CartridgeHandlerException {
		String currenStoreId = getCurrentStoreIdFromNavState(this.getNavigationState());
		if(currenStoreId!=null){

			//add a query modifier to the mdex resource so that we can piggy back an analytics query to the query
			queryModifier = new PriceSliderQueryModifier();
			queryModifier.setCurrentStoreId(currenStoreId);
			YDesignMdexResource resource = (YDesignMdexResource)this.getMdexRequestBroker().getMdexResource();
			resource.getQueryModifiers().add(queryModifier);

			
			//we have to set our filterState to never have a price range filter so that we know the min and the max of the slider
			//of the same state as if the user never moved it.
			if(this.getNavigationState().getFilterState().getRangeFilters().size()>0){
				this.setNavigationState(this.getNavigationState().clearRangeFilters());
				queryModifier.setIndependentQueryMode(true);
				sliderWasUsed=true;
			}
			
		}
		super.preprocess(cartridgeConfig);
	}

	/**
	 * Gets the current store id from nav state.
	 *
	 * @param navigationState the navigation state
	 * @return the current store id from nav state
	 */
	private String getCurrentStoreIdFromNavState(NavigationState navigationState) {
		List<String> recordFilters = navigationState.getFilterState().getRecordFilters();
		for(String filter: recordFilters){
			String storeIdFromRecordFilter = SiteRecordFilterParser.getStoreIdFromRecordFilter(filter);
			if(storeIdFromRecordFilter!=null){
				return storeIdFromRecordFilter;
			}
		}
		return null;
	}

	@Override
	public Breadcrumbs process(BreadcrumbsConfig cartridgeConfig)
			throws CartridgeHandlerException {
		super.process(cartridgeConfig);		

		Breadcrumbs ret = new Breadcrumbs(new BreadcrumbsConfig());
		ret.put("@type", "PriceSlider");

		boolean found=false;
		
		if(queryModifier!=null && queryModifier.priceSortMinMinimum>=0){
			ret.put("priceSortMinMinimum", new Double(queryModifier.priceSortMinMinimum));
			found=true;
		}
		
		if(queryModifier!=null && queryModifier.priceSortMinMaximum>=0){
			ret.put("priceSortMinMaximum", new Double(queryModifier.priceSortMinMaximum));
			found=true;
		}
		
		if(queryModifier!=null && queryModifier.priceSortMaxMinimum>=0){
			ret.put("priceSortMaxMinimum", new Double(queryModifier.priceSortMinMinimum));
			found=true;
		}
		
		if(queryModifier!=null && queryModifier.priceSortMaxMaximum>=0){
			ret.put("priceSortMaxMaximum", new Double(queryModifier.priceSortMinMaximum));
			found=true;
		}
		// if we didn't find any analytics for min and max, return null to hide the price slider
		if(found==false){
			return null;
		}

		try {
			if(getHttpServletRequest().getParameter("price_sort_min")!=null){
				ret.put("currentMin", new Double(getHttpServletRequest().getParameter("price_sort_min")));
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage(),e);
		}
		
		try {
			if(getHttpServletRequest().getParameter("price_sort_max")!=null){
				ret.put("currentMax", new Double(getHttpServletRequest().getParameter("price_sort_max")));
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage(),e);
		}
		
		//Found a case where the remaining result set have all the same price, and the slider was not used, so hide the slider
		if(!sliderWasUsed && ( queryModifier.priceSortMinMinimum == queryModifier.priceSortMinMaximum && queryModifier.priceSortMaxMinimum == queryModifier.priceSortMaxMaximum)){
			if(logger.isDebugEnabled()){
				logger.debug("Found a case where the remaining result set have all the same price, and the slider was not used, so hide the slider");
			}
			return null;
		}
		
		return ret;
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}


}
