package com.aonhewitt.upoint.db;

import java.util.Map;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.alight.cloud.data.store.util.DockerSecretsUtil;

/**
 * DBConnectionProperties responsible toiInitialize all the DB sources connection properties by reading DB configurations
 * @author Yogesh Mittal
 *
 */

@Configuration
@ConfigurationProperties(prefix = "upoint")
//@ConfigurationProperties(prefix = "upoint", ignoreUnknownFields = true) //yogi - Flag to indicate that when binding to this object unknown fields should be ignored
class DBConnectionProperties {

	private static final Logger log = LoggerFactory.getLogger(DBConnectionProperties.class);
	
	// added for geolocation service
	private JndiDataSource geolocation = new JndiDataSource();

	
	/**
	 * Yogesh - Make this array of connection pool objects so that we can control the DB connection properties for each datasource Separately
	 */
	private DataSourceConnection dataSourceConnection = new DataSourceConnection();

	/**
	 *This method should add the properties which are common for different data sources, preventing duplication of same configurations
	 */
	@PostConstruct
	public void initDb() {
		
		Map<String, String> secrets = DockerSecretsUtil.load();
		if(secrets.containsKey("upoint.geolocation.username"))
		{
			geolocation.setUsername(secrets.get("upoint.geolocation.username"));
		}
		if(secrets.containsKey("upoint.geolocation.password"))
		{
			geolocation.setPassword(secrets.get("upoint.geolocation.password"));
		}
	}

	
	// getter setter for data source connection
	public DataSourceConnection getDataSourceConnection() {
		return dataSourceConnection;
	}

	public void setDataSourceConnection(DataSourceConnection dataSourceConnection) {
		this.dataSourceConnection = dataSourceConnection;
	}


    public JndiDataSource getGeolocation() {
    	
	     return geolocation;
	}


	public void setGeolocation(JndiDataSource geolocation) {
		this.geolocation = geolocation;
	}




	
}