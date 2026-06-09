package com.aonhewitt.upoint.pageInfo.model;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class PageInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Object,Object> routes = new HashMap<Object,Object>();
	public Map<Object, Object> getRoutes() {
		return routes; 
	}
	public void setRoutes(Map<Object, Object> routes) {
		this.routes = routes;
	}

	
	
}
