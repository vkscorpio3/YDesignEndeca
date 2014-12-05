package com.cirrus10.endeca.recordhandlers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.cirrus10.endeca.framework.RecordHandler;
import com.endeca.edf.adapter.PVal;
import com.endeca.edf.adapter.Record;
import com.endeca.soleng.javamanipulator.util.RecordUtils;

/**
 * Given a list of fields, create a new property made up of the concatenation of those fields.
 * 
 */
public class ConcatenateProperties implements RecordHandler {
	
	/** The logger. */
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	/** The fields. */
	private List<String> fields;
	
	/** The prop name. */
	private String propName;
	
	/** The separator. */
	private String separator = " ";
	
	/*
	 * Given a list of fields, create a new property made up of the concatenation of those fields.
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cirrus10.endeca.framework.RecordHandler#processRecord(com.endeca.
	 * edf.adapter.Record, com.endeca.soleng.javamanipulator.util.RecordUtils)
	 */
	@Override
	public boolean processRecord(Record rec,
			RecordUtils immutableConvenienceRecord) {
		StringBuffer buff = new StringBuffer();
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("inside processRecord");
		}

		for (String field : getFields()) {
			List<PVal> pvalsList = immutableConvenienceRecord.getProperties(field);
			for(PVal pval:  pvalsList ){
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("Processing " + field + ", value:" + pval.getValue());
				}
				String value = pval.getValue();
				buff.append(value + separator);
			}
		}
		if(buff.length()>0 && this.getPropName()!=null){
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Created a property " + getPropName() + ", value:" + buff.toString());
			}
			rec.add(new PVal(this.getPropName(), buff.toString()));
		}
		return true;
	}

	/**
	 * Gets the fields.
	 *
	 * @return the fields
	 */
	public List<String> getFields() {
		return fields;
	}

	/**
	 * Sets the fields.
	 *
	 * @param fields the new fields
	 */
	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	/**
	 * Gets the prop name.
	 *
	 * @return the prop name
	 */
	public String getPropName() {
		return propName;
	}

	/**
	 * Sets the prop name.
	 *
	 * @param propName the new prop name
	 */
	public void setPropName(String propName) {
		this.propName = propName;
	}

	/**
	 * Gets the separator.
	 *
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Sets the separator.
	 *
	 * @param separator the new separator
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

}
