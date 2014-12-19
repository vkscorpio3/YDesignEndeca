package com.ydg.endeca.cartridge.handler;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.ResultsList;
import com.endeca.infront.cartridge.ResultsListConfig;
import com.endeca.infront.cartridge.ResultsListHandler;
import com.endeca.infront.navigation.NavigationState;
import com.endeca.infront.navigation.model.FilterState;
import com.endeca.infront.navigation.model.MatchMode;
import com.endeca.infront.navigation.model.SearchFilter;

/**
 * The Class CrossSitesResultsListHandler.
 */
public class YDGResultsListAutoSuggestHandler extends ResultsListHandler{

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(YDGResultsListAutoSuggestHandler.class);
	private String typeAheadSearchInterface = "TypeAhead";
	private MatchMode matchMode=MatchMode.ALL;
	
	
	@Override
	public void preprocess(ResultsListConfig arg0)
			throws CartridgeHandlerException {
		replaceSearchInterface();
		super.preprocess(arg0);
		if(logger.isDebugEnabled()){
			logger.debug("inside preprocess()");
		}
	}

	/**
	 * Replace search interface with the one specific for type ahead
	 */
	private void replaceSearchInterface() {

		FilterState newFilterState = this.getNavigationState().getFilterState().clone();
		ArrayList<SearchFilter> newSearchFilters = new ArrayList<SearchFilter>(newFilterState.getSearchFilters());
		for(SearchFilter sf: newSearchFilters){
			sf.setKey(getTypeAheadSearchInterface());
			sf.setMatchMode(getMatchMode());
		}
		
		
		newFilterState.setSearchFilters(newSearchFilters);
		NavigationState newNavState = this.getNavigationState().updateFilterState(newFilterState);
		
		this.setNavigationState(newNavState);
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

	/**
	 * Gets the type ahead search interface.
	 *
	 * @return the type ahead search interface
	 */
	public String getTypeAheadSearchInterface() {
		return typeAheadSearchInterface;
	}

	/**
	 * Sets the type ahead search interface.
	 *
	 * @param typeAheadSearchInterface the new type ahead search interface
	 */
	public void setTypeAheadSearchInterface(String typeAheadSearchInterface) {
		this.typeAheadSearchInterface = typeAheadSearchInterface;
	}

	public MatchMode getMatchMode() {
		return matchMode;
	}

	public void setMatchMode(MatchMode matchMode) {
		this.matchMode = matchMode;
	}


}
