package com.aonhewitt.upoint.modelTester;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aonhewitt.upoint.pageInfo.model.Flavours;
import com.aonhewitt.upoint.pageInfo.model.LinkInfo;
import com.aonhewitt.upoint.pageInfo.model.PageInfo;

@ExtendWith(MockitoExtension.class)

public class ModelTester {
	
	
	private Map<Object,Object> routes = new HashMap<Object,Object>();
	private List<Flavours> flavours = new ArrayList<Flavours>();
	private List<String> links = new ArrayList<String>();
	
	@Test
	public void pageInfoTest(){
		PageInfo pi = new PageInfo();
		pi.setRoutes(routes);
		Map<Object,Object> routeMap = pi.getRoutes();
		assertNotNull(routeMap);
		assertNotNull(pi);

	}
	 
	@Test
	public void linkInfoTest(){
		LinkInfo li = new LinkInfo();
		li.setLinks(links);
		List<String> linkLists  = li.getLinks();
		li.setFlavors(flavours);
		List<Flavours> flavourList = li.getFlavours();
		assertNotNull(linkLists);
		assertNotNull(flavourList);
		assertNotNull(li);

	}
	
	@Test
	public void flavoursTest(){
		Flavours f = new Flavours();
		f.setExpr("ALWAYS_TRUE");
		String exprName = f.getExpr();
		f.setFlavourName("home");
		String name= f.getFlavourName();
		assertNotNull(exprName);
		assertNotNull(name);
		assertNotNull(f);
	}
	
}
