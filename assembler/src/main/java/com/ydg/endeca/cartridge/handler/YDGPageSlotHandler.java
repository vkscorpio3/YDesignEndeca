package com.ydg.endeca.cartridge.handler;

import java.util.List;
import org.apache.log4j.Logger;
import com.endeca.infront.assembler.Assembler;
import com.endeca.infront.assembler.AssemblerException;
import com.endeca.infront.assembler.AssemblerFactory;
import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.assembler.ContentItem;
import com.endeca.infront.cartridge.ContentSlotConfig;
import com.endeca.infront.cartridge.ResultsList;
import com.endeca.infront.content.ContentBroker;
import com.ydg.endeca.utils.CartridgeShare;

/**
 * The Class YDGPageSlotHandler.
 */
public class YDGPageSlotHandler extends com.endeca.infront.cartridge.ContentSlotHandler {
	private static final Logger logger = Logger.getLogger(TwoColumnPageHandler.class);
	private static final Object PROP_NAME_ZERO_RESULTS_PAGE_CONTENT_PATH = "zeroResultsPageContentPath";
	private CartridgeShare cartridgeShare;
	private AssemblerFactory assemblerFactory;
	public ContentBroker getContentBroker() {
		return contentBroker;
	}

	public void setContentBroker(ContentBroker contentBroker) {
		this.contentBroker = contentBroker;
	}

	@Override
	public ContentItem process(ContentItem contentItem)
			throws CartridgeHandlerException {
		ContentItem ret = super.process(contentItem);
		
		
		//check or zero results condition
		if(getCartridgeShare().get(CartridgeShare.SHARED_PROPERTY_RESULTS_LIST)!=null){
			ResultsList results = (ResultsList) this.getCartridgeShare().get(CartridgeShare.SHARED_PROPERTY_RESULTS_LIST);
			if(results.getTotalNumRecs()==0){
				logger.debug("Zero Results condition");
				ContentItem zeroResultsContentItem = null;
				try {
					zeroResultsContentItem = doZeroLandingPage(contentItem);
				} catch (AssemblerException e) {
					logger.error(e.getMessage(), e);
				}
				if(zeroResultsContentItem!=null){
					return zeroResultsContentItem;
				}
			}
		}
		
		
		return ret;
	}

	/**
	 * Do zero landing page.  Makes an assembler call to the content collection zeroResultsPageContentPath.  
	 *
	 * @param config the config
	 * @return the content item.  Or null if there was no page found in that collection.
	 * @throws AssemblerException the assembler exception
	 */
	private ContentItem doZeroLandingPage(ContentItem config) throws AssemblerException {	
		Assembler assembler = getAssemblerFactory().createAssembler();	
		String zeroResultsContentPath = (String) config.get(PROP_NAME_ZERO_RESULTS_PAGE_CONTENT_PATH);		
		if(zeroResultsContentPath!=null && zeroResultsContentPath.startsWith("/content")){
			if(logger.isDebugEnabled()){
				logger.debug("Making assemble call to contentCollection " + zeroResultsContentPath);
			}
			ContentItem contentItem = new ContentSlotConfig(zeroResultsContentPath, 1);
			ContentItem responseContentItem = assembler.assemble(contentItem);	
			if(responseContentItem==null || responseContentItem.get(CONTENTS)==null || ((List)responseContentItem.get(CONTENTS)).isEmpty()  ){
				if(logger.isDebugEnabled()){
					logger.debug("Returning null because content returned was null:" + responseContentItem);
				}
				return null;
			}
			else{
				responseContentItem.put("@type", "PageSlot");
			}
			return responseContentItem;			
		}
		
		return null;
	}



	public CartridgeShare getCartridgeShare() {
		return cartridgeShare;
	}


	public void setCartridgeShare(CartridgeShare cartridgeShare) {
		this.cartridgeShare = cartridgeShare;
	}


	public AssemblerFactory getAssemblerFactory() {
		return assemblerFactory;
	}

	public void setAssemblerFactory(AssemblerFactory assemblerFactory) {
		this.assemblerFactory = assemblerFactory;
	}
}
