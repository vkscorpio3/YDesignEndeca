package com.ydg.endeca.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.endeca.navigation.ENEQuery;
import com.endeca.navigation.ENEQueryException;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.ERecSortKey;
import com.endeca.navigation.ERecSortKeyList;
import com.endeca.navigation.HttpENEConnection;

public class YDesignHttpENEConnection  extends HttpENEConnection{
	private static final Logger logger = Logger.getLogger(YDesignHttpENEConnection.class.getName());
	private static final long serialVersionUID = 1L;
	private ENEQueryResults results;
	private List<QueryModifier> queryModifiers = new ArrayList<QueryModifier>();

	public YDesignHttpENEConnection(String host, int port) {
		super(host,port);
	}

	@Override
	public ENEQueryResults query(ENEQuery query) throws ENEQueryException {
		
		for(QueryModifier qm: getQueryModifiers()){
			qm.modifyQuery(query);
		}
		if(logger.isDebugEnabled()){
			dumpENEQuery(query);
		}
		
		ENEQueryResults ret = super.query(query);
		
		
		for(QueryModifier qm: getQueryModifiers()){
			qm.addResults(ret);
		}
		
		setResults(ret);
		return ret;
	}

	public ENEQueryResults getResults() {
		return results;
	}

	public void setResults(ENEQueryResults results) {
		this.results = results;
	}

	public List<QueryModifier> getQueryModifiers() {
		return queryModifiers;
	}

	public void setQueryModifiers(List<QueryModifier> queryModifiers) {
		this.queryModifiers = queryModifiers;
	}
	

    public static void dumpENEQuery(ENEQuery q) {
		if(logger.isDebugEnabled()) {
			logger.debug( "Query: ENEQuery Num requested: " + q.getNavNumERecs() + " starting at record " + q.getNavERecsOffset());
			logger.debug("Query: ENEQuery Aggregated Num requested: " + q.getNavNumAggrERecs() + " starting at record " + q.getNavAggrERecsOffset());

	    	if (q.getNavERecSearchDidYouMean())
				logger.debug("Query: ENEQuery Did You Mean: TRUE.");
			else
				logger.debug("Query: ENEQuery Did You Mean: FALSE.");
	    	if (q.getNavRollupKey() != null)
				logger.debug("Query: ENEQuery Nav Rollup Key: " + q.getNavRollupKey());
			else
				logger.debug("Query: No ENEQuery Nav Rollup Key.");
	    	if (q.getAnalyticsQuery() != null)
				logger.debug("Query: ENEQuery Analytics Query: " + q.getAnalyticsQuery());
			else
				logger.debug("Query: No ENEQuery Analytics Query.");
			if (q.getNavRecordFilter() != null)
				logger.debug("Query: ENEQuery Record Filter: " + q.getNavRecordFilter());
			else
				logger.debug("Query: No ENEQuery Record Filter.");
			if (q.getNavRangeFilters() != null)
				logger.debug("Query: ENEQuery Range Filters: " + q.getNavRangeFilters().toString());
			else
				logger.debug("Query: No ENEQuery Range Filters.");
			if (q.getNavRecordStructureExpr() != null)
				logger.debug("Query: ENEQuery Record Strcture Exp: " + q.getNavRecordStructureExpr());
			else
				logger.debug("Query: No ENEQuery Record Strcture Exp.");
			if (q.getSelection() != null)
				logger.debug("Query: ENEQuery Selected Fields: " + q.getSelection().toString());
			else
				logger.debug("Query: No ENEQuery Selected Fields.");
			if (q.getNavDescriptors() != null)
				logger.debug("Query: ENEQuery Refinements: " + q.getNavDescriptors().toString());
			else
				logger.debug("Query: No ENEQuery Refinements.");
			if (q.getNavERecSearches() != null)
				logger.debug("Query: ENEQuery Searches: " + q.getNavERecSearches().toString());
			else
				logger.debug("Query: No ENEQuery Searches.");
			if (q.getNavActiveSortKeys() != null)
				dumpENESortKeys(q.getNavActiveSortKeys());
			else
				logger.debug("Query: No ENEQuery Sorts.");
			if (q.getNavRelRankERecRank() != null)
				logger.debug("Query: ENEQuery Ranking: " + q.getNavRelRankERecRank().toString());
			else
				logger.debug("Query: No ENEQuery Rel Ranking.");
	
			if (q.getDimSearchTerms() != null) // only report if terms are set
				logger.debug("Query: ENEQuery Num Dim Values requested: " + q.getDimSearchNumDimValues());
			if (q.getDimSearchNavRecordStructureExpr() != null)
				logger.debug("Query: ENEQuery Dim Search Record Strcture Exp: " + q.getDimSearchNavRecordStructureExpr());
			if (q.getDimSearchNavDescriptors() != null)
				logger.debug("Query: ENEQuery Dim Search Refinements: " + q.getDimSearchNavDescriptors().toString());
			if (q.getDimSearchNavRangeFilters() != null)
				logger.debug("Query: ENEQuery Dim Search Range Filters: " + q.getDimSearchNavRangeFilters().toString());
			if (q.getDimSearchNavRecordFilter() != null)
				logger.debug("Query: ENEQuery Dim Search Record Filter: " + q.getDimSearchNavRecordFilter());
			if (q.getDimSearchTerms() != null)
				logger.debug("Query: ENEQuery Dim Search Terms: " + q.getDimSearchTerms());
			if (q.getDimSearchTerms() != null) // only report if terms are set
				logger.debug("Query: ENEQuery Dim Search Dimension: " + q.getDimSearchDimension());
			if (q.getDimSearchTerms() != null) // only report if terms are set
				logger.debug("Query: ENEQuery Dim Search Compound: " + q.getDimSearchCompound());
			if (q.getDimSearchTerms() != null) // only report if terms are set
				logger.debug("Query: ENEQuery Dim Search Options: " + q.getDimSearchOpts());
			if (q.getDimSearchTerms() != null) // only report if terms are set
				logger.debug("Query: ENEQuery Dim Search Rank: " + q.getDimSearchRankResults());
		}
    }
    

	private static void dumpENESortKeys(ERecSortKeyList ask) {
        for(int i=0;i<ask.size();i++) {
	        ERecSortKey sk = ask.getKey(i);
	        String order = "desc";
	        if (sk.getOrder()==ERecSortKey.ASCENDING)
	        	order = "asc";
			logger.debug( "Query: ENEQuery Sort " + i+1 + ": " + sk.getName() + " is being sorted in " + order + " order.");
		}
	}
}
