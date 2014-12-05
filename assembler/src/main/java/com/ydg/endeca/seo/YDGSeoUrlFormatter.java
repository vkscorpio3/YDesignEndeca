package com.ydg.endeca.seo;

import com.endeca.soleng.urlformatter.UrlFormatException;
import com.endeca.soleng.urlformatter.UrlState;
import com.endeca.soleng.urlformatter.seo.SeoUrlFormatter;

public class YDGSeoUrlFormatter extends SeoUrlFormatter {

	@Override
	public String formatUrl(UrlState pUrlState) throws UrlFormatException {
		pUrlState.removeParam("Nr");
		return super.formatUrl(pUrlState);
	}

}
