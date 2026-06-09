package com.aonhewitt.upoint.pageInfo.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;


@Component
public class Flavours implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String flavourName;
	private String expr;

	public String getFlavourName() {
		return flavourName;
	}

	public void setFlavourName(String flavourName) {
		this.flavourName = flavourName;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

}
