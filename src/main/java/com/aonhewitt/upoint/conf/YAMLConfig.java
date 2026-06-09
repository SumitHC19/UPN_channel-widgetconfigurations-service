package com.aonhewitt.upoint.conf;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.util.ObjectUtils;

@ConfigurationProperties
@Component
public class YAMLConfig {

	// No more additions without discussion.
	// Do not use setters directly
	// use getYamlConfig method
    @JsonInclude(Include.NON_NULL)
	private Map<Object, Object> gaConfigurations;
	private String ivaJavaScriptSrc;

	public String getIvaJavaScriptSrc() {
		return ivaJavaScriptSrc;
	}

	public void setIvaJavaScriptSrc(String ivaJavaScriptSrc) {
		this.ivaJavaScriptSrc = ivaJavaScriptSrc;
	}

	public Map<Object, Object> getGaConfigurations() {
		return gaConfigurations;
	}

	public void setGaConfigurations(Map<Object, Object> gaConfigurations) {
		this.gaConfigurations = gaConfigurations;
	}

	public Map<Object, Object> getYamlConfig() {

		Map<Object, Object> configMap = new HashMap<Object, Object>();

		if (!ObjectUtils.isEmpty(gaConfigurations)) {
			configMap.put("gaConfigurations", gaConfigurations);
		}

		if (!ObjectUtils.isEmpty(ivaJavaScriptSrc)) {
			configMap.put("ivaJavaScriptSrc", ivaJavaScriptSrc);
		}

		return configMap;
	}

}
