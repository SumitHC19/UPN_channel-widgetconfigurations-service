package com.aonhewitt.upoint.upc.watcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.aonhewitt.upoint.cache.util.AppConfigReader;
import com.aonhewitt.upoint.util.ConfigurationListUtils;


@Configuration
@EnableScheduling
public class UPCConfigWatcher {
		
		
		@Autowired
		private ConfigurationListUtils configurationListUtils;
		
		
		
		@Scheduled(fixedDelayString = "#{ ${app.upcJVMCacheClear.TTL:18} * 60000 }")
		public void UpdateUpcConfigCache() 
		{
				
			if (AppConfigReader.getInstance().isUpcJVMCacheClear())
			{
			configurationListUtils.clearUpcConfigCache();
			}
			
		}

	}