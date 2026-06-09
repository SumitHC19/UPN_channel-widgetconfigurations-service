package com.aonhewitt.upoint.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aonhewitt.portal.linkres.runtime.html.AnchorTag;
import com.aonhewitt.portal.linkres.runtime.html.Tag;
import com.aonhewitt.portal.linkres.runtime.link.Link;
import com.aonhewitt.upoint.cache.util.ShortCacheUtil;

public class MockShortCacheUtil {
	private static ShortCacheUtil  shaCacheUtil = null;
	public static ShortCacheUtil getInstance() {
		shaCacheUtil = new ShortCacheUtil();
		return shaCacheUtil;
	}
	
	public  Map<String, Map<String, List<String>>> getAfLinksCache() {
		Map<String,Map<String,List<String>>> afLinksCache = new ConcurrentHashMap<String,Map<String,List<String>>>();
		List<String> linkNames = new ArrayList<String>();
		linkNames.add("Link1");
		linkNames.add("Link2");
		Map<String,List<String>> assetGroupMap = new HashMap<String, List<String>>();
		assetGroupMap.put("AssetGroup1", linkNames);
		afLinksCache.put("00095_1.0", assetGroupMap);
		return afLinksCache;
	}
	
	public Map<String,Map<String,Link>> getUptLinksCache() {
		Map<String, Map<String, Link>> uptLinksCache= new ConcurrentHashMap<String, Map<String,Link>>();
		Map<String, Link> linkmap = new HashMap<String,Link>();
		Link l1 = new Link();
		l1.setId("l1");
		Link l2 = new Link();
		l2.setId("l2");
		linkmap.put("l1",l1);
		linkmap.put("l2",l2);
		uptLinksCache.put("00095_1.0", linkmap);
		return uptLinksCache;
	}
	

	public Map<String,Map<String,Link>> getUceLinksCache() {
		Map<String, Map<String, Link>> uceLinksCache=new ConcurrentHashMap<String, Map<String,Link>>();
		Map<String, Link> linkmap = new HashMap<String,Link>();
		Link l1 = new Link();
		l1.setId("l1");
		Link l2 = new Link();
		l2.setId("l2");
		linkmap.put("l1",l1);
		linkmap.put("l2",l2);
		uceLinksCache.put("00095_1.0", linkmap);
		return uceLinksCache;
	}
	
	public Map<String,Map<String,Tag>> getUptLinkTagCache() {
		Map<String, Map<String, Tag>> uptLinkTagCache=new ConcurrentHashMap<String, Map<String,Tag>>();
		Map<String, Tag> tagmap = new HashMap<String,Tag>();
		Tag lt1 = new AnchorTag();
		Tag lt2 = new AnchorTag();
		tagmap.put("lt1",lt1);
		tagmap.put("lt2",lt2);
		uptLinkTagCache.put("00095_1.0", tagmap);
		return uptLinkTagCache;
	}
	
	public Map<String,Map<String,Tag>> getUceLinkTagCache() {
		Map<String, Map<String, Tag>> uceLinkTagCache=new ConcurrentHashMap<String, Map<String,Tag>>();
		Map<String, Tag> tagmap = new HashMap<String,Tag>();
		Tag lt1 = new AnchorTag();
		Tag lt2 = new AnchorTag();
		tagmap.put("lt1",lt1);
		tagmap.put("lt2",lt2);
		uceLinkTagCache.put("00095_1.0", tagmap);
		return uceLinkTagCache;
	}
	
	public Map<String, Map<String, Boolean>> getUptExprCache() {
		Map<String, Map<String, Boolean>> uptExprCache= new HashMap<String, Map<String, Boolean>>();
		 Map<String, Boolean> exprMap= new HashMap<String, Boolean>();
		 exprMap.put("ALWAYS_PASS", Boolean.TRUE);
		 exprMap.put("ALWAYS_FAIL", Boolean.FALSE);
		 uptExprCache.put("00095_1.0", exprMap);
		return uptExprCache;
	}
	
	public Map<String, Map<String, Boolean>> getUceExprCache() {
		Map<String, Map<String, Boolean>> uceExprCache=new HashMap<String, Map<String, Boolean>>();
		 Map<String, Boolean> exprMap= new HashMap<String, Boolean>();
		 exprMap.put("ALWAYS_PASS", Boolean.TRUE);
		 exprMap.put("ALWAYS_FAIL", Boolean.FALSE);
		 uceExprCache.put("00095_1.0", exprMap);
		return uceExprCache;
	}

}

