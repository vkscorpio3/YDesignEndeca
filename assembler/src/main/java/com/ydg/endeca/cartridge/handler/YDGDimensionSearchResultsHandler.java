package com.ydg.endeca.cartridge.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.DimensionSearchResults;
import com.endeca.infront.cartridge.DimensionSearchResultsConfig;
import com.endeca.infront.cartridge.DimensionSearchResultsHandler;
import com.endeca.infront.cartridge.model.DimensionSearchGroup;
import com.endeca.infront.cartridge.model.DimensionSearchValue;
import com.ydg.endeca.action.TypeAheadActionPathProvider;

public class YDGDimensionSearchResultsHandler extends DimensionSearchResultsHandler {
	private static final Logger logger = Logger.getLogger(YDGDimensionSearchResultsHandler.class);

	@Override
	public void preprocess(DimensionSearchResultsConfig cartridgeConfig)
			throws CartridgeHandlerException {
		if(logger.isDebugEnabled()){
			logger.debug("Inside preprocess");
		}
		TypeAheadActionPathProvider actionProvider = new TypeAheadActionPathProvider();
		actionProvider.setNavigationActionPath((String)cartridgeConfig.get("actionPathPrefix"));
		this.setActionPathProvider(actionProvider);		
		super.preprocess(cartridgeConfig);
	}

	@Override
	public DimensionSearchResults process(
			DimensionSearchResultsConfig cartridgeConfig)
			throws CartridgeHandlerException {
		if(logger.isDebugEnabled()){
			logger.debug("Inside process");
		}
		DimensionSearchResults ret = super.process(cartridgeConfig);
		overrideNavigationActionContentPath(ret, "Brands", "search");
		return ret;
	}

	/**
	 * Overrides the navigation action content path with some other value.
	 * 
	 * This is to meet the requirement that dimension results for Brand should 
	 * take them to the search results page where that Brand is selected as a refinement
	 * 
	 *
	 * @param ret the ret
	 * @param dimensionName the dimension name
	 * @param pagePath the page path that will be appended to the existing contentPath
	 */
	private void overrideNavigationActionContentPath(DimensionSearchResults ret, String dimensionName,
			String pagePath) {
		if(ret==null){
			return;
		}
		
		//iterate through groups
		List<DimensionSearchGroup> searchGroups = ret.getDimensionSearchGroups();
		for(DimensionSearchGroup group: searchGroups){

			//if the dimensionName matches dimensionName parameter passed to this method
			if(dimensionName.equals(group.getDimensionName())){

				//iterate through dimVals so we can replace the navigation action content path
				List<DimensionSearchValue> dimValues = group.getDimensionSearchValues();
				for(DimensionSearchValue dimVal: dimValues){
					String contentPath = dimVal.getContentPath();
					
					//build the new content path
					StringBuffer newPath = new StringBuffer();
					if(contentPath!=null && contentPath.isEmpty()==false){
						newPath.append(contentPath);
						if(!contentPath.endsWith("/")){
							newPath.append("/");
						}
						newPath.append(pagePath);
					}
					
					//set the new content path
					dimVal.setContentPath(newPath.toString());
				}
			}
		}
	}

}
