package com.ydg.endeca.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.endeca.infront.navigation.model.MdexResource;
import com.endeca.navigation.ENEConnection;

public class YDesignMdexResource extends MdexResource {
	private static final Logger logger = Logger.getLogger(YDesignMdexResource.class.getName());
	private List<YDesignHttpENEConnection> connections = new ArrayList<YDesignHttpENEConnection>();
	private List<QueryModifier> queryModifiers = new ArrayList<QueryModifier>();

	
	@Override
	public ENEConnection makeConnection() {
		
		YDesignHttpENEConnection conn= new YDesignHttpENEConnection(getHost(), getPort());
		
		//pass on query modifier that is set on this object to also be in the Connection
		conn.setQueryModifiers(getQueryModifiers());
		
		connections.add(conn);
		return conn;
	}


	public List<YDesignHttpENEConnection> getConnections() {
		return connections;
	}


	public void setConnections(List<YDesignHttpENEConnection> connections) {
		this.connections = connections;
	}


	public List<QueryModifier> getQueryModifiers() {
		return queryModifiers;
	}


	public void setQueryModifiers(List<QueryModifier> queryModifiers) {
		this.queryModifiers = queryModifiers;
	}

}
