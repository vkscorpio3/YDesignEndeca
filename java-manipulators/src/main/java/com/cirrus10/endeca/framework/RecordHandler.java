package com.cirrus10.endeca.framework;


import com.endeca.edf.adapter.Record;
import com.endeca.soleng.javamanipulator.util.RecordUtils;

public interface RecordHandler {

	/**
	 * Process record.  Return false if you want to remove the record.  True if you want to keep the record.
	 *
	 * @param rec the rec
	 * @param immutableConvenienceRecord 
	 * @return true, if if you want to keep the record.
	 */
	boolean processRecord(Record rec, RecordUtils immutableConvenienceRecord);

}
