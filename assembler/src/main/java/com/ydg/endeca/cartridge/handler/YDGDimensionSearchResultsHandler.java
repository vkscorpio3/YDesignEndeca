package com.ydg.endeca.cartridge.handler;

import org.apache.log4j.Logger;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.DimensionSearchResults;
import com.endeca.infront.cartridge.DimensionSearchResultsConfig;
import com.endeca.infront.cartridge.DimensionSearchResultsHandler;

public class YDGDimensionSearchResultsHandler extends DimensionSearchResultsHandler {
	private static final Logger logger = Logger.getLogger(YDGDimensionSearchResultsHandler.class);

	@Override
	public void preprocess(DimensionSearchResultsConfig cartridgeConfig)
			throws CartridgeHandlerException {
		if(logger.isDebugEnabled()){
			logger.debug("Inside preprocess");
		}
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
		
		
		return ret;
		
	}

}
