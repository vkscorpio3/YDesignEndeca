package com.ydg.endeca.cartridge.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.assembler.ContentItem;
import com.endeca.infront.cartridge.NavigationCartridgeHandler;
import com.endeca.infront.cartridge.RefinementMenu;
import com.ydg.endeca.utils.CartridgeShare;
import com.ydg.endeca.utils.PostprocessCartridgeHandler;

public class GuidedNavigationHandler extends NavigationCartridgeHandler<ContentItem, ContentItem> implements PostprocessCartridgeHandler{
	private static final Logger logger = Logger.getLogger(GuidedNavigationHandler.class);
	private  ContentItem guidedNavigationContentItem=null;
	private CartridgeShare cartridgeShare;

	
	@Override
	public ContentItem process(ContentItem pContentItem)
			throws CartridgeHandlerException {
		/*Register self to postProcess cartridges so that our postProcess method will get executed later*/
		CartridgeShare share = getCartridgeShare();
		share.getPostProcessCartridges().add(this);		
		
		
		guidedNavigationContentItem = pContentItem;
		return pContentItem;
	}

	@Override
	protected ContentItem wrapConfig(ContentItem config) {
		return config;
	}

	@Override
	public void postProcess() {
		if(guidedNavigationContentItem==null){
			return;
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("Inside process");
		}
		
		//clean up any empty refinement menu, made possible by processing inline breadcrumb enabled refinement menus
		List<RefinementMenu> toRemove = new ArrayList<RefinementMenu>();
		List<Object> navigation = (List<Object>) guidedNavigationContentItem.get("navigation");
		if(navigation!=null){
			for(Object obj: navigation){
				if(obj instanceof RefinementMenu){
					RefinementMenu refMenu = (RefinementMenu)obj;
					if(refMenu.getRefinements()==null || refMenu.getRefinements().isEmpty()){
						toRemove.add(refMenu);
					}
				}
			}
			
			if(!toRemove.isEmpty()){
				for(RefinementMenu remove: toRemove){
					navigation.remove(remove);
				}
			}
		}
	}

	public CartridgeShare getCartridgeShare() {
		return cartridgeShare;
	}

	public void setCartridgeShare(CartridgeShare cartridgeShare) {
		this.cartridgeShare = cartridgeShare;
	}

}
