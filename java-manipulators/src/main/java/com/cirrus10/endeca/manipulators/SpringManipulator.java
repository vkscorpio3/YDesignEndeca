package com.cirrus10.endeca.manipulators;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.endeca.edf.adapter.Adapter;
import com.endeca.edf.adapter.AdapterConfig;
import com.endeca.edf.adapter.AdapterException;
import com.endeca.edf.adapter.AdapterHandler;

public class SpringManipulator implements Adapter {
	
	public static final String PASSTHROUGH_PROPERTY_NAME="BEAN_NAME";
	
	@Override
	public void execute(AdapterConfig config, AdapterHandler handler)
			throws AdapterException {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-manipulator.xml");
		Adapter manipulator = (Adapter) context.getBean(config.get(PASSTHROUGH_PROPERTY_NAME)[0]);
		manipulator.execute(config, handler);
	}
}