package com.aonhewitt.upoint.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import com.aonhewitt.portal.configuration.modules.assetmanagement.model.AssetDTO;
import com.aonhewitt.upoint.pageInfo.model.Flavours;
import com.aonhewitt.upoint.pageInfo.model.LinkInfo;
import com.aonhewitt.upoint.pageInfo.model.PageInfo;
import org.springframework.util.ObjectUtils;

@Component
public class PageInfoUtil {

	

	/*
	 * public PageInfo getPageInfo() { return pageInfo; }
	 * 
	 * @Autowired public void setPageInfo(PageInfo pageInfo) { this.pageInfo =
	 * pageInfo; }
	 */

	// New method for DCH-2211
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getPageInfoFromAsset(Map<Object, Object> recvdAssets) {
		PageInfo pageInfo = new PageInfo();
		Map<Object, Object> pageInfoMap = new LinkedHashMap<Object, Object>();
		Map<Object, Object> finalMap = new LinkedHashMap<Object, Object>();

		Map<String, AssetDTO> baseAfAssets = Objects.nonNull(recvdAssets.get("AF_Pages_List"))
				? (Map<String, AssetDTO>) recvdAssets.get("AF_Pages_List") : MapUtils.EMPTY_MAP;
		Map<String, AssetDTO> clientAfAssets = Objects.nonNull(recvdAssets.get("AF_Client_Pages_List"))
				? (Map<String, AssetDTO>) recvdAssets.get("AF_Client_Pages_List") : MapUtils.EMPTY_MAP;

		Map<Object, AssetDTO> afAssets = Stream
				.concat(baseAfAssets.entrySet().stream(), clientAfAssets.entrySet().stream()).collect(Collectors.toMap(
						entry -> entry.getKey(), entry -> entry.getValue(), (baseValue, clientValue) -> clientValue));
		
		if (MapUtils.isNotEmpty(afAssets)) {
			afAssets.forEach((k, v) -> createPageInfo(k, v, pageInfoMap, pageInfo));
		}
		finalMap.put("assetRouters", pageInfo);
		return finalMap;
	}

	// New method for DCH-2211
	private void createPageInfo(Object k, AssetDTO value, Map<Object, Object> pageInfoMap, PageInfo pageInfo) {
		LinkInfo linkInfo = new LinkInfo();

		String str = (String) value.getAssetKey();
		if (!ObjectUtils.isEmpty(str) && str.length() > 0) {
			String[] arr = str.split(";");
			String flavours = "";
			String links = "";

			if (arr.length > 1) {
				flavours = arr[0];
				links = arr[1];
			} else {
				links = str;
			}

			List<Flavours> flavorList = createFlavorData(flavours);
			List<String> linkList = createLinkData(links);
			if (CollectionUtils.isNotEmpty(flavorList)) {
				linkInfo.setFlavors(flavorList);
			}
			if (CollectionUtils.isNotEmpty(linkList)) {
				linkInfo.setLinks(linkList);
			}

			pageInfoMap.put(k, linkInfo);
			pageInfo.setRoutes(pageInfoMap);
		}
	}

	// New method for DCH-2211
	public List<Flavours> createFlavorData(String flavorInfo) {
		List<Flavours> flavorList = null;

		if (flavorInfo.length() > 8) {
			flavorInfo = flavorInfo.substring(8);
			String[] flavors = flavorInfo.split("#");
			flavorList = new ArrayList<Flavours>();

			for (String flavor : flavors) {
				String[] individualFlavor = flavor.split("\\|");
				Flavours flaObj = new Flavours();

				if (individualFlavor.length > 1) {
					flaObj.setFlavourName(individualFlavor[0]);
					flaObj.setExpr(individualFlavor[1]);
				} else {
					flaObj.setFlavourName(individualFlavor[0]);
				}

				flavorList.add(flaObj);
			}
		}

		return flavorList;
	}

	// New method for DCH-2211
	public List<String> createLinkData(String linkInfo) {
		List<String> linkList = null;
		if (linkInfo.length() > 6) {
			linkInfo = linkInfo.substring(6);
			String[] individualLink = linkInfo.split("#");
			linkList = new ArrayList<String>();
			for (String link : individualLink) {
				linkList.add(link);
			}
		}
		return linkList;
	}
}
