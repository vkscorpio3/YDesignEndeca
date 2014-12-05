package com.cirrus10.endeca.config;

public class CreatePropBasedOnOtherPropsConfig {
	private String propertyName;
	private String propertyValue;
	private String newPropertyToCreate;
	private String newPropertyValueToCreate;
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getNewPropertyToCreate() {
		return newPropertyToCreate;
	}
	public void setNewPropertyToCreate(String newPropertyToCreate) {
		this.newPropertyToCreate = newPropertyToCreate;
	}
	public String getNewPropertyValueToCreate() {
		return newPropertyValueToCreate;
	}
	public void setNewPropertyValueToCreate(String newPropertyValueToCreate) {
		this.newPropertyValueToCreate = newPropertyValueToCreate;
	}
	public CreatePropBasedOnOtherPropsConfig(String propertyName,
			String propertyValue, String newPropertyToCreate,
			String newPropertyValueToCreate) {
		super();
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.newPropertyToCreate = newPropertyToCreate;
		this.newPropertyValueToCreate = newPropertyValueToCreate;
	}
	public CreatePropBasedOnOtherPropsConfig() {
		super();
	}
	
}
