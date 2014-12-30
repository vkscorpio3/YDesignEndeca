package com.ydg.endeca.cartridge.handler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.Breadcrumbs;
import com.endeca.infront.cartridge.RefinementMenu;
import com.endeca.infront.cartridge.RefinementMenuConfig;
import com.endeca.infront.cartridge.RefinementMenuHandler;
import com.endeca.infront.cartridge.model.Refinement;
import com.endeca.infront.cartridge.model.RefinementBreadcrumb;
import com.ydg.endeca.utils.CartridgeShare;
import com.ydg.endeca.utils.PostprocessCartridgeHandler;
import com.ydg.endeca.utils.RefinementComparator;

public class YDGRefinementMenuHandler extends RefinementMenuHandler implements PostprocessCartridgeHandler {
	private static final Logger logger = Logger.getLogger(YDGRefinementMenuHandler.class);
	private CartridgeShare cartridgeShare;
	RefinementMenu refMenuToReturn;
	RefinementMenuConfig theCartridgeConfig;
	String theDimensionName;

	@Override
	public void preprocess(RefinementMenuConfig cartridgeConfig)
			throws CartridgeHandlerException {
		this.theCartridgeConfig = cartridgeConfig;
		super.preprocess(cartridgeConfig);
	}

	@Override
	public RefinementMenu process(RefinementMenuConfig cartridgeConfig)
			throws CartridgeHandlerException {
		
		RefinementMenu ret =  super.process(cartridgeConfig);
		
		//if this refinement menu is set to have inline breadcrumbs
		if(cartridgeConfig.getBooleanProperty("enableInlineCrumbs", false)){
			//get dimension name for later use
			this.theDimensionName=(String)cartridgeConfig.get("dimensionName");

			/*Register self to postProcess cartridges so that our postProcess method will get executed later*/
			CartridgeShare share = getCartridgeShare();
			share.getPostProcessCartridges().add(this);		
			
			//if there was no refinement menu retrieved from calling super.process method, create a RefinementMenu to return anyway
			//because it's possible that all refinements have already been selected, thus will only be in the breadcrumb.
			//if that's the case we'll insert all the selected refinement from the breadcrumb into this RefinementMenu
			if(ret == null){
				ret = new RefinementMenu(cartridgeConfig);
			}
			
			this.refMenuToReturn = ret;
		}
		
		return ret;
	}

	@Override
	public void postProcess() {
		if(logger.isDebugEnabled()){
			logger.debug("Inside post process");
		}
		
		Breadcrumbs breadcrumb = (Breadcrumbs)getCartridgeShare().get("breadcrumb");
		if(breadcrumb==null){
			return;
		}
		
		List<RefinementBreadcrumb>  refinementCrumbs = findRefinementCrumbForThisDimension(theDimensionName, breadcrumb);
		if(refinementCrumbs  ==null || refinementCrumbs.isEmpty()){
			return;
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("Found a refinement crumb, going to insert the refinementCrumb to the refinementMenu");
		}
		
		List<Refinement> refinementsToAdd = buildRefinementsFromCrumb(refinementCrumbs);
		
		if(refMenuToReturn.getRefinements()==null){
			refMenuToReturn.setRefinements(new ArrayList<Refinement>());
		}
		
		//int originalCount = refMenuToReturn.getRefinements().size();
		
		//add the inline breadcrumb refinements
		if(refinementsToAdd!=null && !refinementsToAdd.isEmpty() && refMenuToReturn!=null){
			refMenuToReturn.getRefinements().addAll(refinementsToAdd);
		}
		
		//re-sort the refinements alphabetically
		Collections.sort(refMenuToReturn.getRefinements(), new RefinementComparator() );
		
		/*if original count was > 0, let's trim it again to that original count
		if(originalCount>0){
			refMenuToReturn.setRefinements(refMenuToReturn.getRefinements().subList(0, originalCount));
		}*/
	}

	private List<Refinement> buildRefinementsFromCrumb(
			List<RefinementBreadcrumb> refinementCrumbs) {
		ArrayList<Refinement> ret = new ArrayList<Refinement>();
		if(refinementCrumbs==null||refinementCrumbs.isEmpty()){
			return ret;
		}
		
		for(RefinementBreadcrumb refCrumb: refinementCrumbs){
			Refinement refinement = new Refinement();
			refinement.setLabel(refCrumb.getLabel());
			refinement.setCount(refCrumb.getCount());
			
			//set the contentPath for the action
			refinement.setContentPath(this.getActionPathProvider().getDefaultNavigationActionContentPath());
			//set the navigationState to be equivalent to the remove action of the breadcrumb
			refinement.setNavigationState(refCrumb.getRemoveAction().getNavigationState());

			refinement.setProperties(refCrumb.getProperties());
			if(refinement.getProperties()==null){
				refinement.setProperties(new HashMap<String, String>());
			}	
			
			//create a Property "selected" so it can be used to mark the checkbox selected.		
			refinement.getProperties().put("selected", "true");
			
			ret.add(refinement);
		}
		
		return ret;
	}

	
	/**
	 * Find refinement crumb for this dimension.
	 *
	 * @param dimensionName the dimension name
	 * @param breadcrumb the breadcrumb
	 * @return the list
	 */
	private List<RefinementBreadcrumb> findRefinementCrumbForThisDimension(
			String dimensionName, Breadcrumbs breadcrumb) {
		List<RefinementBreadcrumb> ret = new ArrayList<RefinementBreadcrumb>();

		
		if(dimensionName==null){
			if(logger.isDebugEnabled()){
				logger.debug("Dimension name was null, returning null");
				return ret;
			}
		}
		
		List<RefinementBreadcrumb> refinementCrumbs = breadcrumb.getRefinementCrumbs();
		for(RefinementBreadcrumb refCrumb: refinementCrumbs){
			if(refCrumb.getDimensionName().equals(dimensionName)){
				ret.add(refCrumb);
			}
		}
		return ret;
	}

	public CartridgeShare getCartridgeShare() {
		return cartridgeShare;
	}

	public void setCartridgeShare(CartridgeShare cartridgeShare) {
		this.cartridgeShare = cartridgeShare;
	}

}
