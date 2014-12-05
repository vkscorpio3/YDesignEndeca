package com.ydg.endeca.cartridge.handler;

import org.apache.log4j.Logger;
import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.ResultsList;
import com.endeca.infront.cartridge.ResultsListConfig;
import com.endeca.infront.cartridge.ResultsListHandler;

/**
 * The Class CrossSitesResultsListHandler.
 */
public class YDGResultsListAutoSuggestHandler extends ResultsListHandler{

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(YDGResultsListAutoSuggestHandler.class);

	@Override
	public void preprocess(ResultsListConfig arg0)
			throws CartridgeHandlerException {
		super.preprocess(arg0);
		if(logger.isDebugEnabled()){
			logger.debug("inside preprocess()");
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
		return ret;
	}
}
