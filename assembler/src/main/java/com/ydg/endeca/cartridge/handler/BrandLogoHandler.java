package com.ydg.endeca.cartridge.handler;

import java.util.ArrayList;
import java.util.List;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.Breadcrumbs;
import com.endeca.infront.cartridge.BreadcrumbsConfig;
import com.endeca.infront.cartridge.BreadcrumbsHandler;
import com.endeca.infront.cartridge.model.RefinementBreadcrumb;

public class BrandLogoHandler extends  BreadcrumbsHandler {
	private String brandDimensionName="Brands";
	
	public String getBrandDimensionName() {
		return brandDimensionName;
	}

	public void setBrandDimensionName(String brandDimensionName) {
		this.brandDimensionName = brandDimensionName;
	}

	@Override
	public Breadcrumbs process(BreadcrumbsConfig cartridgeConfig)
			throws CartridgeHandlerException {
		Breadcrumbs breadcrumb =  super.process(new BreadcrumbsConfig());
		breadcrumb.put("@type", "BrandLogo");

		List<RefinementBreadcrumb> refinementCrumbs =  breadcrumb.getRefinementCrumbs();
		List<RefinementBreadcrumb> brandCrumbs = new ArrayList<RefinementBreadcrumb>();
		
		//look for the brand crumb
		for(RefinementBreadcrumb refCrumb: refinementCrumbs){
			String dimensionName = refCrumb.getDimensionName();
			if(getBrandDimensionName().equals(dimensionName)){
				brandCrumbs.add( refCrumb );
			}
		}
		
		if(!brandCrumbs.isEmpty() && brandCrumbs.size()==1){
			RefinementBreadcrumb brandCrumb = brandCrumbs.get(0);
			Breadcrumbs ret = new Breadcrumbs(cartridgeConfig);
			ret.put("name", brandCrumb.getLabel());
			//if the dimVal has dimValProperties, pass it all into the return object
			if(!brandCrumb.getProperties().isEmpty()){
				ret.putAll(brandCrumb.getProperties());
			}
			return ret;
		}
		return null;
	}
}
