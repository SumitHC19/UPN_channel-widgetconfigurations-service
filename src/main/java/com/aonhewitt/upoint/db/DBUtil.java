package com.aonhewitt.upoint.db;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
/**
 * @author - Yogesh Mittal
 * 
 *         This class will construct the ContextResource for geolocation db
 *         sources by reading DB configurations which are required for JNDI
 *         connections
 */

@Component
public class DBUtil {
	@Autowired
	DBConnectionProperties dBConnectionProperties;

	public void setdBConnectionProperties(DBConnectionProperties dBConnectionProperties) {
		this.dBConnectionProperties = dBConnectionProperties;
	}

	// create context resource for geolocation
	public ContextResource getGeoLocationContextResource()

	{
		ContextResource resourceBase = null;

		if (StringUtils.isNotEmpty(dBConnectionProperties.getGeolocation().getJndi())
				&& StringUtils.isNotEmpty(dBConnectionProperties.getGeolocation().getDriverClassName())
				&& StringUtils.isNotEmpty(dBConnectionProperties.getGeolocation().getUrl())
				&& StringUtils.isNotEmpty(dBConnectionProperties.getGeolocation().getUsername())
				&& StringUtils.isNotEmpty(dBConnectionProperties.getGeolocation().getPassword()))

		{
			resourceBase = new ContextResource();
			resourceBase.setType(DataSource.class.getName());
			resourceBase.setName(dBConnectionProperties.getGeolocation().getJndi());
			resourceBase.setProperty("driverClassName", dBConnectionProperties.getGeolocation().getDriverClassName());
			resourceBase.setProperty("url", dBConnectionProperties.getGeolocation().getUrl());
			resourceBase.setProperty("username", dBConnectionProperties.getGeolocation().getUsername());
			resourceBase.setProperty("password", dBConnectionProperties.getGeolocation().getPassword());
			String defaultSchema = dBConnectionProperties.getGeolocation().getDefaultSchema();
			if (!ObjectUtils.isEmpty(defaultSchema) && !"".equals(defaultSchema)) {
				System.setProperty("geolocationDefaultSchema", defaultSchema);
			}
			System.setProperty("geolocationDBUrl", dBConnectionProperties.getGeolocation().getUrl());
			setDBConnectionPoolSettings(resourceBase);
		}
		return resourceBase;
	}

	private void setDBConnectionPoolSettings(ContextResource contextResource) {
		dBConnectionProperties.getDataSourceConnection().getProperties().forEach((key, value) -> {
			if (!ObjectUtils.isEmpty(value)) {
				contextResource.setProperty(key, value);
			}
		});
	}
}
