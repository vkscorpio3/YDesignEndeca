package com.cirrus10.endeca.recordhandlers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cirrus10.endeca.config.CreatePropBasedOnOtherPropsConfig;
import com.cirrus10.endeca.framework.RecordHandler;
import com.endeca.edf.adapter.PVal;
import com.endeca.edf.adapter.Record;
import com.endeca.soleng.javamanipulator.util.RecordUtils;

/**
 * The Class CreatePropBasedOnOtherProps. If a record contains a particular
 * property with a particular value, then it creates a new property/value pair.
 * 
 */
public class CreatePropBasedOnOtherProps implements RecordHandler {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	private List<CreatePropBasedOnOtherPropsConfig> configurations;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cirrus10.endeca.framework.RecordHandler#processRecord(com.endeca.
	 * edf.adapter.Record, com.endeca.soleng.javamanipulator.util.RecordUtils)
	 */
	@Override
	public boolean processRecord(Record rec,
			RecordUtils immutableConvenienceRecord) {

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("inside processRecord");
		}

		for (CreatePropBasedOnOtherPropsConfig config : getConfigurations()) {
			PVal value = immutableConvenienceRecord.getFirstProperty(config
					.getPropertyName());
			if (value != null) {
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("got value back: " + value.getValue());
				}
				if (config.getPropertyValue().equals(value.getValue())) {
					
					if (logger.isLoggable(Level.FINE)) {
						logger.fine("Created a new property" + config.getNewPropertyToCreate() + "=" + config.getNewPropertyValueToCreate());
					}
					
					rec.add(new PVal(config.getNewPropertyToCreate(), config
							.getNewPropertyValueToCreate()));
				}
			}
		}
		return true;
	}

	public List<CreatePropBasedOnOtherPropsConfig> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(
			List<CreatePropBasedOnOtherPropsConfig> configurations) {
		this.configurations = configurations;
	}
}
