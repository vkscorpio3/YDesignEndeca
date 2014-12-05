package com.cirrus10.endeca.recordhandlers;

import com.cirrus10.endeca.framework.RecordHandler;
import com.endeca.edf.adapter.PVal;
import com.endeca.edf.adapter.Record;
import com.endeca.soleng.javamanipulator.util.RecordUtils;

public class SampleRecordHandler implements RecordHandler {

	public SampleRecordHandler() {
		super();
	}

	@Override
	public boolean processRecord(Record rec,
			RecordUtils immutableConvenienceRecord) {
		rec.add(new PVal("samplerecordhandler-property", "hello world"));
		return true;
	}

}
