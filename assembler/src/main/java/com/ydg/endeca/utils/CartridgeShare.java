package com.ydg.endeca.utils;
import java.util.ArrayList;
import java.util.List;

import com.endeca.infront.assembler.BasicContentItem;

/**
 * The Class CartridgeShare.
 */
public class CartridgeShare extends BasicContentItem{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	public static String SHARED_PROPERTY_BREADCRUMB = "breadcrumb";
	public static String SHARED_PROPERTY_RESULTS_LIST = "resultsList";

	
	/** The post process cartridges. */
	private List<PostprocessCartridgeHandler> postProcessCartridges = new ArrayList<PostprocessCartridgeHandler>();
	
	
	/**
	 * Gets the post process cartridges.
	 *
	 * @return the post process cartridges
	 */
	public List<PostprocessCartridgeHandler> getPostProcessCartridges() {
		return postProcessCartridges;
	}
	
	/**
	 * Sets the post process cartridges.
	 *
	 * @param postProcessCartridges the new post process cartridges
	 */
	public void setPostProcessCartridges(
			List<PostprocessCartridgeHandler> postProcessCartridges) {
		this.postProcessCartridges = postProcessCartridges;
	}
}
