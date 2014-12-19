package com.ydg.endeca.query;

import com.endeca.navigation.ENEQuery;
import com.endeca.navigation.ENEQueryResults;

public interface QueryModifier {
	public void modifyQuery(ENEQuery query);
	public void addResults(ENEQueryResults results);

}
