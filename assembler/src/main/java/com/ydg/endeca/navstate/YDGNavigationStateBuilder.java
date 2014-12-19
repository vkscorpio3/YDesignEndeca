package com.ydg.endeca.navstate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.endeca.infront.navigation.NavigationException;
import com.endeca.infront.navigation.NavigationState;
import com.endeca.infront.navigation.model.FilterState;
import com.endeca.infront.navigation.model.RangeFilter;
import com.endeca.infront.navigation.url.UrlNavigationStateBuilder;
import com.ydg.endeca.site.Site;
import com.ydg.endeca.site.SiteDirectory;

public class YDGNavigationStateBuilder extends UrlNavigationStateBuilder {

	private static final String PATH_DELIMITER = "/";
	private static final int SITE_ID_TOKEN_LOCATION = 1;
	private SiteDirectory siteDirectory;
	private static final Logger logger = Logger
			.getLogger(YDGNavigationStateBuilder.class);
	private HttpServletRequest httpServletRequest;

	@Override
	public NavigationState parseNavigationState(String queryString,
			String pathInfo, String characterEncoding)
			throws NavigationException {
		NavigationState ret = super.parseNavigationState(queryString, pathInfo,
				characterEncoding);
		ret = addSiteFilters(ret, pathInfo);
		ret = addPriceSliderFilters(ret);
		return ret;
	}

	/**
	 * Adds the price slider filters.
	 * 
	 * @param ret
	 *            the ret
	 * @return the navigation state
	 */
	private NavigationState addPriceSliderFilters(NavigationState ret) {
		FilterState filterState = ret.getFilterState();
		List<RangeFilter> rangeFilters = filterState.getRangeFilters();
		boolean modified = false;

		// create min price
		if (getHttpServletRequest().getParameter("price_sort_min") != null) { //TODO make this into constant
			try {
				// create minimum price
				RangeFilter minimumPrice = new RangeFilter();
				minimumPrice.setPropertyName("price_sort_min"); //TODO make this into constant
				minimumPrice.setOperation(RangeFilter.Operation.GTEQ);
				minimumPrice.setBound1(new Double(getHttpServletRequest()
						.getParameter("price_sort_min")));
				rangeFilters.add(minimumPrice);
				modified = true;
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
			}
		}

		// create max price
		if (getHttpServletRequest().getParameter("price_sort_max") != null) { //TODO make this into constant
			try {
				RangeFilter maxPrice = new RangeFilter();
				maxPrice.setPropertyName("price_sort_max");
				maxPrice.setOperation(RangeFilter.Operation.LTEQ);
				maxPrice.setBound1(new Double(getHttpServletRequest().getParameter(
						"price_sort_max")));
				rangeFilters.add(maxPrice);
				modified = true;
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
			}
		}

		if (modified) {
			filterState.setRangeFilters(rangeFilters);
			return ret.updateFilterState(filterState);
		} else {
			return ret;
		}
	}

	private NavigationState addSiteFilters(NavigationState navState,
			String pathInfo) {
		if (pathInfo == null || navState == null) {
			return navState;
		}
		String tokens[] = pathInfo.split(PATH_DELIMITER);
		if (tokens != null && tokens.length >= SITE_ID_TOKEN_LOCATION + 1) {
			String contextPath = tokens[SITE_ID_TOKEN_LOCATION];
			if (contextPath != null) {
				Site site = this.getSiteDirectory().getMapByContextPath()
						.get(contextPath);
				if (site != null) {
					String siteId = site.getId();
					List<String> recordFilters = navState.getFilterState()
							.getRecordFilters();
					recordFilters.add("Store_id:" + siteId);
					if (logger.isDebugEnabled()) {
						logger.debug("Adding site record filter: "
								+ "Store_id:" + siteId);
					}
					FilterState filterStateCopy = navState.getFilterState()
							.clone();
					filterStateCopy.setRecordFilters(recordFilters);
					return navState.updateFilterState(filterStateCopy);
				}
			}
		}
		return navState;
	}

	/**
	 * Gets the site directory.
	 * 
	 * @return the site directory
	 */
	public SiteDirectory getSiteDirectory() {
		return siteDirectory;
	}

	/**
	 * Sets the site directory.
	 * 
	 * @param siteDirectory
	 *            the new site directory
	 */
	public void setSiteDirectory(SiteDirectory siteDirectory) {
		this.siteDirectory = siteDirectory;
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}
}
