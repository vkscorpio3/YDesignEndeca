package com.ydg.endeca.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class SiteRecordFilterParser. This parses a string that is in the format
 * of an endeca record filter, to find the store_id specified in the record
 * filter
 */
public class SiteRecordFilterParser {
	private static String REGEXP = "Store_id:(\\w*)";
	private static Pattern pattern = Pattern.compile(REGEXP);

	public static String getStoreIdFromRecordFilter(String recordFilter) {
		if(recordFilter==null){
			return null;
		}
		
		Matcher matcher = pattern.matcher(recordFilter);
		if (matcher.find()) {
			//System.out.println("Start index: " + matcher.start());
			//System.out.println(" End index: " + matcher.end() + " ");
			//System.out.println(" Group count " + matcher.groupCount());

			//System.out.println(matcher.group());
			if (matcher.groupCount() > 0) {
				//System.out.println("Group: " + matcher.group(1));
				return matcher.group(1);
			}
		}
		else{
			//System.out.println("does not match");
		}
		return null;
	}
}
