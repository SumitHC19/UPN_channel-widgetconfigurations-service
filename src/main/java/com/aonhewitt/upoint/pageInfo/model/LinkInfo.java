package com.aonhewitt.upoint.pageInfo.model;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Component
@JsonInclude(Include.NON_NULL)
public class LinkInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Flavours> flavours ;
	private List<String> links ;
	public List<Flavours> getFlavours() {
		return flavours;
	}
	public void setFlavors(List<Flavours> flavours) {
		this.flavours = flavours;
	}
	public List<String> getLinks() {
		return links;
	}
	public void setLinks(List<String> links) {
		this.links = links;
	}
	
	
	
}
