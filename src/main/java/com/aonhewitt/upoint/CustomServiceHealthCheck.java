
package com.aonhewitt.upoint;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.alight.logging.helpers.InfoTypeLogEventHelper;
import com.aonhewitt.logging.events.ErrorLogEvent;
import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
import com.aonhewitt.upoint.cache.util.AppConfigReader;
import com.aonhewitt.upoint.cache.util.ShortCacheUtil;

@Component
public class CustomServiceHealthCheck implements HealthIndicator {

	@Autowired
	AppConfigReader appConfigReader;
	
	@Autowired
	ShortCacheUtil shortCacheUtil;
	
	private boolean isWidgetHealthGreen = false;
	
	@Override
	public Health health() {
		try {
			boolean isClientShortCacheEnabled = appConfigReader.isEnableClientShortCache();
			boolean isCLentShortCacheSchedulerEnabled = appConfigReader.isClientShortCacheSchedulerEnabled();
			boolean baseCacheForCorePortalParmsInitializationInProgress = shortCacheUtil.isBaseShortCacheForCorePortalParamsInitializationInProgress();
			boolean clientCacheInitializationInProgress = shortCacheUtil.isClientShortCacheInitializationInProgress();
			 
			if ((isClientShortCacheEnabled == true && isCLentShortCacheSchedulerEnabled == true 
					&& (clientCacheInitializationInProgress || baseCacheForCorePortalParmsInitializationInProgress))
					|| (isClientShortCacheEnabled == false  && baseCacheForCorePortalParmsInitializationInProgress)) {	
				return Health.down().build();
			}
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(CustomServiceHealthCheck.class.getName(),
					"Channel-WidgetConfigurations service custom health check facing issue..!!",
					"health()",
					e, ErrorLogEvent.ERROR_SEVERITY);
			printLogs(isWidgetHealthGreen);
			return Health.up().build();
		}
		
		printLogs(isWidgetHealthGreen);
		return Health.up().build();
	}
	
	void printLogs(boolean isWidgetHealthGreen){
		if(!isWidgetHealthGreen){
			this.isWidgetHealthGreen = true;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS a zzz");
			long endTime = System.currentTimeMillis();
			NumberFormat formatter = new DecimalFormat("#0.00000");
			InfoTypeLogEventHelper.logInfoEvent(CustomServiceHealthCheck.class.getName(), "channel-widgetconfigurations is Up at "+dateFormat.format(new Date()) +
					". Total time taken to be Up and Running is: "+ formatter.format((endTime - Long.valueOf(System.getProperty("startTime"))) / 1000d) + " seconds");
		}
	}
}