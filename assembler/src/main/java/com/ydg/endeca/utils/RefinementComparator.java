package com.ydg.endeca.utils;

import java.util.Comparator;

import com.endeca.infront.cartridge.model.Refinement;

public class RefinementComparator implements Comparator<Refinement> {

	@Override
	public int compare(Refinement r1, Refinement r2) {
		if (r1 == null || r1.getLabel()==null) {
			return 1;
		}
		if (r2 == null || r2.getLabel()==null ) {
			return -1;
		}
		return r1.getLabel().toLowerCase().compareTo(r2.getLabel().toLowerCase());
	}

}
