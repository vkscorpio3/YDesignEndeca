package com.cirrus10.endeca.manipulators;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cirrus10.endeca.framework.RecordHandler;
import com.cirrus10.endeca.recordhandlers.SampleRecordHandler;
import com.endeca.edf.adapter.Adapter;
import com.endeca.edf.adapter.AdapterConfig;
import com.endeca.edf.adapter.AdapterException;
import com.endeca.edf.adapter.AdapterHandler;
import com.endeca.edf.adapter.Record;
import com.endeca.soleng.javamanipulator.util.RecordUtils;

public  class BaseManipulator implements Adapter {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private List<RecordHandler> recordHandlers;
	
	/* (non-Javadoc)
	 * @see com.endeca.edf.adapter.Adapter#execute(com.endeca.edf.adapter.AdapterConfig, com.endeca.edf.adapter.AdapterHandler)
	 */
	public void execute(AdapterConfig config, AdapterHandler handler) 	throws AdapterException{
		
		// loop through all input sources of this manipulator
		init();
		
		for (int inp = 0; inp != handler.getNumInputs(); inp++)
		{
			
			boolean hasMoreRecords = true;

			// loop through all of the records for the current input
			while(hasMoreRecords)
			{
				// get the records, one by one, from the current input source.
				Record rec = handler.getRecord(inp);
				// when we are out
				if (rec != null)
				{
					
					boolean emit=true;
					
					for(RecordHandler recHandler: this.getRecordHandlers()){
						//an immutable convenience record for easy reading of properties and values
						RecordUtils immutableConvenienceRecord = new RecordUtils(rec);
						emit = recHandler.processRecord(rec, immutableConvenienceRecord);
						if(emit==false){
							break;
						}
					}
					
					// we must emit() each record we want to pass out of this
					// manipulator into the next downstream component 
					if(emit){
						handler.emit(rec);
					}
					else{
						logger.log(Level.FINE, "Skipping record because emit was false");
					}
				} else {
					hasMoreRecords = false;
				}
			}
		}
	}


	public void init() {
		//recordHandlers.add(new SampleRecordHandler());
	}

	public List<RecordHandler> getRecordHandlers() {
		return recordHandlers;
	}

	public void setRecordHandlers(List<RecordHandler> recordHandlers) {
		this.recordHandlers = recordHandlers;
	}
}


