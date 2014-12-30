package com.ydg.endeca.cartridge.handler;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.Breadcrumbs;
import com.endeca.infront.cartridge.BreadcrumbsConfig;
import com.endeca.infront.cartridge.BreadcrumbsHandler;
import com.ydg.endeca.utils.CartridgeShare;

/**
 * The Class CrossSitesResultsListHandler.
 */
public class YDGBreadcrumbsHandler extends BreadcrumbsHandler{
	private CartridgeShare cartridgeShare;


	@Override
	public Breadcrumbs process(BreadcrumbsConfig cartridgeConfig)
			throws CartridgeHandlerException {
		Breadcrumbs ret =  super.process(cartridgeConfig);
		
		//share the breadcrumb so that other cartridges may have access to it.
		getCartridgeShare().put("breadcrumb", ret);
		
		return ret;
	}
	

	public CartridgeShare getCartridgeShare() {
		return cartridgeShare;
	}

	public void setCartridgeShare(CartridgeShare cartridgeShare) {
		this.cartridgeShare = cartridgeShare;
	}
}
  