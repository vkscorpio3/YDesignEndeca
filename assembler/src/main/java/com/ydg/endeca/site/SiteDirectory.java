package com.ydg.endeca.site;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The Class SiteDirectory.  Contains a list of all the known sites.  Also contains maps that will make looking up by
 * siteId or contextPath easy.
 */
public class SiteDirectory {
	
	/** The sites. */
	private List<Site> sites;
	
	/** The map by id. */
	private Map<String, Site> mapById;
	
	/** The map by context path. */
	private Map<String, Site> mapByContextPath;
	
	/**
	 * Gets the sites.
	 *
	 * @return the sites
	 */
	public List<Site> getSites() {
		return sites;
	}

	/**
	 * Sets the sites.
	 *
	 * @param sites the new sites
	 */
	public void setSites(List<Site> sites) {
		this.sites = sites;
	}
	
	/**
	 * Builds the different maps.
	 */
	public void init(){
		mapById = new HashMap<String, Site>();
		mapByContextPath = new HashMap<String, Site>();
		
		
		if(sites==null){
			return;
		}
		
		for(Site site: sites){
			mapById.put(site.getId(), site);
			mapByContextPath.put(site.getContextPath(), site);
		}
	}

	/**
	 * Gets the map by id.
	 *
	 * @return the map by id
	 */
	public Map<String, Site> getMapById() {
		return mapById;
	}

	/**
	 * Sets the map by id.
	 *
	 * @param mapById the map by id
	 */
	public void setMapById(Map<String, Site> mapById) {
		this.mapById = mapById;
	}

	/**
	 * Gets the map by context path.
	 *
	 * @return the map by context path
	 */
	public Map<String, Site> getMapByContextPath() {
		return mapByContextPath;
	}

	/**
	 * Sets the map by context path.
	 *
	 * @param mapByContextPath the map by context path
	 */
	public void setMapByContextPath(Map<String, Site> mapByContextPath) {
		this.mapByContextPath = mapByContextPath;
	}
}
