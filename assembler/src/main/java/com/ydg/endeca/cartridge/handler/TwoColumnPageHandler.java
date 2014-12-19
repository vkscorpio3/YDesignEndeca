package com.ydg.endeca.cartridge.handler;

import java.util.List;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.assembler.ContentItem;
import com.endeca.infront.cartridge.NavigationCartridgeHandler;
import com.ydg.endeca.utils.CartridgeShare;
import com.ydg.endeca.utils.PostprocessCartridgeHandler;

public class TwoColumnPageHandler extends NavigationCartridgeHandler<ContentItem, ContentItem> {
	private CartridgeShare cartridgeShare;

	@Override
	protected ContentItem wrapConfig(ContentItem paramContentItem) {
		return paramContentItem;
	}

	@Override
	public void preprocess(ContentItem pContentItem)
			throws CartridgeHandlerException {
	}

	@Override
	public ContentItem process(ContentItem pContentItem)
			throws CartridgeHandlerException {
		List<PostprocessCartridgeHandler> postprocessCartridges = getCartridgeShare().getPostProcessCartridges();
		for(PostprocessCartridgeHandler cartridge : postprocessCartridges ){
			cartridge.postProcess();
		}
		return pContentItem;
	}

	public CartridgeShare getCartridgeShare() {
		return cartridgeShare;
	}

	public void setCartridgeShare(CartridgeShare cartridgeShare) {
		this.cartridgeShare = cartridgeShare;
	}

}
