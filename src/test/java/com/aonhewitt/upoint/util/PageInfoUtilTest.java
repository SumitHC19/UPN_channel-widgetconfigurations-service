package com.aonhewitt.upoint.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.aonhewitt.portal.configuration.modules.assetmanagement.model.AssetDTO;


//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
public class PageInfoUtilTest {
	
	/*//@MockBean
	PageInfo pageInfo = new PageInfo();*/
	
	//@InjectMocks
	PageInfoUtil pageUtil = new PageInfoUtil();


	@BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }
	
	@Test
	public void createData(){
		Map<Object,Object> assets = new HashMap<Object,Object>();
		Map<Object, AssetDTO> afAssets = new HashMap<Object, AssetDTO>();
		AssetDTO asset = new AssetDTO("","","flavour:home|AF_HOME#thrivehome|UPOINT_THRIVE_PPT_ENABLED;links:GBL_NAV_HOME#PRTL_DRT#OverViewLink#ACCESS_CUSTOMER_ECS_SRCH_LINK#LINK_ITEM_TEMPLATE#prtlHome");
		AssetDTO asset2 = new AssetDTO("","","flavour:financialwellbeing|AF_FWB;links:UPN_FWB_ASSESSMENT_LINK");
		AssetDTO asset3 = new AssetDTO("","","links:UPN_FWB_ASSESSMENT_LINK");
		AssetDTO asset4 = new AssetDTO("","","links:UPN_FWB_ASSESSMENT_LINK#test2");
		AssetDTO asset5 = new AssetDTO("","","flavour:test1#test2|always;links:UPN_FWB_ASSESSMENT_LINK");
		
		afAssets.put("multipleFalvor", asset);
		afAssets.put("oneFlavorExprOneLink", asset2);
		afAssets.put("oneLinkNoFlavor", asset3);
		afAssets.put("moreLinkNoFlavor", asset4);
		afAssets.put("oneFlavorWoExprSecFlavorExprMoreLinks", asset5);

		Map<Object, AssetDTO> clientAfAssets = new HashMap<Object, AssetDTO>();
		AssetDTO clAsset1 = new AssetDTO("","","flavour:thrivehome|UPOINT_THRIVE_PPT_ENABLED;links:GBL_NAV_HOME");
		AssetDTO clAsset2 = new AssetDTO("","","flavour:home|AF_HOME#thrivehome|UPOINT_THRIVE_PPT_ENABLED;links:GBL_NAV_HOME");
		clientAfAssets.put("home", clAsset1);
		clientAfAssets.put("thriveHome", clAsset2);
		
		assets.put("AF_Pages_List", afAssets);
		assets.put("AF_Client_Pages_List", clientAfAssets);
		Map<Object, Object> finalMap = pageUtil.getPageInfoFromAsset(assets) ;
		/*Gson gson = new Gson();
		System.out.println(gson.toJson(finalMap)); */
		
		assertTrue(finalMap.size()>0); 
		
	}
}
