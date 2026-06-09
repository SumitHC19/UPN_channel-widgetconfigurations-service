package com.aonhewitt.upoint.core.controller.udp;

/**
 * @author AH0144608
 * 
 * UDP Rest Controller is going to help get data from UDP
 *
 */
//@RestController
//@RequestMapping("/resolveConfig/")
public class UDPRestController {/*

	@Autowired
	UDPManagerService uDPManagerService;
	
	@PostMapping(value = "udp", produces = "application/json")
	public List<Map<Object,Object>> invokeUdp(@RequestBody UDPRequestBean aRequestBean) {
		List<Map<Object,Object>> response=new ArrayList<Map<Object,Object>>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		UDPInvocationResult serviceResponseResult = null;
		try {
			serviceResponseResult = uDPManagerService.invokeUdp(aRequestBean);
			if(serviceResponseResult.getStatusCode()==0){
			//	map.put("UDP",serviceResponseResult.getResponse());
				response.add(map);
			}
			else{
				Map<String,String> mapOfUdpError=new HashMap<String,String>();
				mapOfUdpError.put("Error", serviceResponseResult.toString());
				map.put("UDP",mapOfUdpError);
				response.add(map);
			}
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(UDPRestController.class.getName(), "Exception while resolving UDP data", "invokeUdp method failed", e,
					ErrorLogEvent.ERROR_SEVERITY);
			Map<Object,Object> errormap=new HashMap<Object,Object>();
			Map<String,String> exceptionMap=new HashMap<String,String>();
			exceptionMap.put("Error",e.getMessage() );
			errormap.put("UDP",exceptionMap);
			response.add(errormap);
		}
		return response;
		
	}
	
	
*/}