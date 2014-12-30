package com.ydg.endeca.cartridge.handler;

import java.util.List;
import java.util.Map;
//import org.apache.log4j.Logger;
import com.endeca.infront.assembler.BasicContentItem;
import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.assembler.ContentItem;
import com.endeca.infront.cartridge.Breadcrumbs;
import com.endeca.infront.cartridge.NavigationCartridgeHandler;
import com.endeca.infront.cartridge.model.RefinementBreadcrumb;
import com.ydg.endeca.utils.CartridgeShare;
import com.ydg.endeca.utils.GlobalConstants;
import com.ydg.endeca.utils.PostprocessCartridgeHandler;

public class CategoryHeadingHandler extends NavigationCartridgeHandler<ContentItem, ContentItem> implements PostprocessCartridgeHandler{
	private CartridgeShare cartridgeShare;
	//private static final Logger logger = Logger.getLogger(CategoryHeadingHandler.class);
	private ContentItem categoryHeadingModel;	
	private String templateId = "CategoryHeading";
	private static final String PROP_CATEGORY_NAME = "categoryName";
	
	@Override
	protected ContentItem wrapConfig(ContentItem config) {
		return config;
	}

	@Override
	public void preprocess(ContentItem pContentItem)
			throws CartridgeHandlerException {
		super.preprocess(pContentItem);
	}

	/* (non-Javadoc)
	 * @see com.endeca.infront.cartridge.NavigationCartridgeHandler#process(com.endeca.infront.assembler.ContentItem)
	 */
	@Override
	public ContentItem process(ContentItem pContentItem)
			throws CartridgeHandlerException {
		
		/*
		 * Register self to postProcess cartridges, and return an empty model object.
		 * The model object will be populated later on in the postprocess() method
		 * 
		 */
		
		/*Register self to postProcess cartridges so that our postProcess method will get executed later*/
		getCartridgeShare().getPostProcessCartridges().add(this);		
		categoryHeadingModel = new BasicContentItem(pContentItem);
		categoryHeadingModel.put("@type", getTemplateId());
		return categoryHeadingModel;
	}

	public CartridgeShare getCartridgeShare() {
		return cartridgeShare;
	}

	public void setCartridgeShare(CartridgeShare cartridgeShare) {
		this.cartridgeShare = cartridgeShare;
	}

	@Override
	public void postProcess() {
		Breadcrumbs  breadcrumb = (Breadcrumbs)getCartridgeShare().get(CartridgeShare.SHARED_PROPERTY_BREADCRUMB);
		if(breadcrumb!=null){
			//look for category refinement
			List<RefinementBreadcrumb> refinementCrumbs = breadcrumb.getRefinementCrumbs();
			if(refinementCrumbs!=null){
				for(RefinementBreadcrumb refCrumb: refinementCrumbs){
					if(GlobalConstants.DIMENSION_NAME_PRODUCT_CATEGORY.equals(refCrumb.getDimensionName())){
						/*
						 * Found the category breadcrumb, add the category name and its properties
						 * into the model object
						 */
						this.categoryHeadingModel.put(PROP_CATEGORY_NAME, refCrumb.getLabel());
						Map<String, String> properties = refCrumb.getProperties();
						if(properties!=null){
							this.categoryHeadingModel.putAll(properties);
						}
					}
				}
			}
		}
	}


	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
