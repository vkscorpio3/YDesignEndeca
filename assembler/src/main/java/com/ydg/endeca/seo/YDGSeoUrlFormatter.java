package com.ydg.endeca.seo;

import com.endeca.soleng.urlformatter.UrlFormatException;
import com.endeca.soleng.urlformatter.UrlState;
import com.endeca.soleng.urlformatter.seo.SeoUrlFormatter;

public class YDGSeoUrlFormatter extends SeoUrlFormatter {

	@Override
	public String formatUrl(UrlState pUrlState) throws UrlFormatException {
		pUrlState.removeParam("Nr");
		pUrlState.removeParam("format");
		//clear all from breadcrumbs means Nf does not exist at all
		if(pUrlState.getParam("Nf")==null){
			pUrlState.removeParam("price_sort_min");
			pUrlState.removeParam("price_sort_max");
		}

		if(pUrlState.getParam("Nf")!=null){
			pUrlState.removeParam("Nf");
		}
		
		return super.formatUrl(pUrlState);
	}

}
