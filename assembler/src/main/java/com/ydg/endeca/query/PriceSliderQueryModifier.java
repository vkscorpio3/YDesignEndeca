package com.ydg.endeca.query;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.endeca.navigation.AnalyticsStatementResult;
import com.endeca.navigation.ENEQuery;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.ERec;
import com.endeca.navigation.PropertyMap;
import com.endeca.navigation.analytics.AnalyticsQuery;
import com.ydg.endeca.utils.SiteRecordFilterParser;

public class PriceSliderQueryModifier implements QueryModifier {
	private static final Logger logger = Logger
			.getLogger(PriceSliderQueryModifier.class);
	boolean propertyAdded;
	public double priceSortMinMinimum = -1;
	public double priceSortMinMaximum = -1;
	public double priceSortMaxMinimum = -1;
	public double priceSortMaxMaximum = -1;

	
	public double maximumPrice = -1;
	private String currentStoreId;
	private static final String PRICE_SORT_MIN = "price_sort_min";
	private static final String PRICE_SORT_MAX = "price_sort_max";

	
	//when this is false, we're most likely merged with other cartridge's queries
	//if set to true we're most likely our own query
	private boolean independentQueryMode = false;
	
	public String getCurrentStoreId() {
		return currentStoreId;
	}

	public void setCurrentStoreId(String currentStoreId) {
		this.currentStoreId = currentStoreId;
	}

	@Override
	public void modifyQuery(ENEQuery query) {
		// only append the analytics query if we think it's the right query
		// that actually is for the results list query.
		// a good indication for that is looking at query.getNavNumERecs >0
		if (appendAnalytics(query)) {
			

			if (logger.isDebugEnabled()) {
				logger.debug("Adding analytics query to get price min and max");				
			}

			String analyticsQuery = createAnalyticsQuery();
			AnalyticsQuery analytics = AnalyticsQuery.parseQuery(analyticsQuery);
			query.setAnalyticsQuery(analytics);

			//if we are our own query, set the query not ask for any records to make it more efficient
			if(isIndependentQueryMode()){
				logger.debug("Detected that we're in independent query mode");
			}

		}
	}

	/**
	 * Decides if we need to append analytics.
	 *
	 * @param query the query
	 * @return true, if successful
	 */
	private boolean appendAnalytics(ENEQuery query) {
		//make sure that the current store id matches the storeId from the query's record filter.
		//if it doesn't match, then most likely it's for the cross site results list cartridge
		//which we're not interested in.		
		String storeIdFromQuery = SiteRecordFilterParser.getStoreIdFromRecordFilter(query.getNavRecordFilter());
		
		
		if (getCurrentStoreId().equals(storeIdFromQuery)) {
			//if we are piggy backing along with other cartridges, a good indicator is that it's requesting for records 
			if(!isIndependentQueryMode() && query.getNavNumERecs() > 0 && query.getNavRangeFilters() == null){
				return true;
			}
			
			//if we're an independent query, we don't necessarily request for any records, so 
			//we do not have to check on record count
			if(isIndependentQueryMode() && query.getNavRangeFilters() == null){
				return true;
			}
		}
		return false;
	}

	private String createAnalyticsQuery() {
		StringBuffer query = getAnalyticsQueryHeader("minMaxValues");
		addPropertyToQuery(query, PRICE_SORT_MIN);
		addPropertyToQuery(query, PRICE_SORT_MAX);
		updateAnalyticsQueryFooter(query);
		return query.toString();
	}

	private StringBuffer getAnalyticsQueryHeader(String returnValue) {
		StringBuffer analyticsQuery = new StringBuffer();
		analyticsQuery.append("RETURN \"" + returnValue + "\" AS SELECT ");
		return analyticsQuery;
	}

	private void addPropertyToQuery(StringBuffer analyticsQuery, String propertyName) {
		propertyAdded = true;
		analyticsQuery.append("MIN(\"" + propertyName + "\") AS \"" + propertyName
				+ ".MIN\", MAX(\"" + propertyName + "\") AS \"" + propertyName + ".MAX\", ");
	}

	private void updateAnalyticsQueryFooter(StringBuffer analyticsQuery) {
		analyticsQuery.deleteCharAt(analyticsQuery.length() - 2);
		analyticsQuery.append("GROUP");
	}

	@Override
	public void addResults(ENEQueryResults results) {
		// inspect if this result contains analytics query results

		// TODO - add some qualifier here to check if the query returned results
		// for records

		AnalyticsStatementResult analyticsResults;
		analyticsResults = results.getNavigation().getAnalyticsStatementResult(
				"minMaxValues");
		if (analyticsResults != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Got back analytics results");
				parseResults(analyticsResults);
			}
		}
	}

	/**
	 * Parses the results. Sets this.minimumPrice and this.maximumPrice if it
	 * finds it from analytics results.
	 * 
	 * @param analyticsResults
	 *            the analytics results
	 */
	private void parseResults(AnalyticsStatementResult analyticsResults) {
		Iterator<ERec> recordIter = analyticsResults.getERecIter();
		while (recordIter.hasNext()) {
			ERec analyticsRec = (ERec) recordIter.next();
			PropertyMap propMap = analyticsRec.getProperties();
			if (propMap != null) {
				// get minimum
				this.priceSortMinMinimum = getAnalyticsResultProperty("price_sort_min.MIN", propMap);
				this.priceSortMinMaximum = getAnalyticsResultProperty("price_sort_min.MAX", propMap);
				this.priceSortMaxMinimum = getAnalyticsResultProperty("price_sort_max.MIN", propMap);
				this.priceSortMaxMaximum = getAnalyticsResultProperty("price_sort_max.MAX", propMap);
			}
		}
	}

	/**
	 * Gets the analytics result property.  Returns the double value of the string representation.
	 *
	 * @param propName the prop name
	 * @param propMap the prop map
	 * @return the analytics result property
	 */
	private double getAnalyticsResultProperty(String propName, PropertyMap propMap) {
		String value = (String) propMap.get(propName);
		if(value!=null){
			try {
				return new Double(value);			
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return -1;
	}

	public boolean isIndependentQueryMode() {
		return independentQueryMode;
	}

	public void setIndependentQueryMode(boolean independentQueryMode) {
		this.independentQueryMode = independentQueryMode;
	}
}
