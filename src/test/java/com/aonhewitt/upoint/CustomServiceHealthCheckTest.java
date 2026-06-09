package com.aonhewitt.upoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aonhewitt.upoint.cache.util.AppConfigReader;
import com.aonhewitt.upoint.cache.util.ShortCacheUtil;

@ExtendWith(MockitoExtension.class)
public class CustomServiceHealthCheckTest {
   @InjectMocks
   private CustomServiceHealthCheck customServiceHealthCheck;
   @Mock
   private AppConfigReader appConfigReader;

   @Mock
   private ShortCacheUtil shortCacheUtil;

   @BeforeEach
   public void setUp() {
      MockitoAnnotations.openMocks(this);
      System.setProperty("startTime", String.valueOf(System.currentTimeMillis()));
   }

   @Test
   @DisplayName("Test Health Check with ShortCache False")
   public void healthWithShortCacheFalse() throws Exception {
         Mockito.doReturn(false).when(appConfigReader).isEnableClientShortCache();
         Mockito.doReturn(false).when(appConfigReader).isClientShortCacheSchedulerEnabled();
  //       Mockito.doReturn(false).when(shortCacheUtil).isClientShortCacheInitializationInProgress();
         customServiceHealthCheck.health();
   }

   @Test
   @DisplayName("Test Health Check with ShortCache True")
   public void healthWithShortCacheTrue() throws Exception {
      Mockito.doReturn(true).when(appConfigReader).isEnableClientShortCache();
      Mockito.doReturn(true).when(appConfigReader).isClientShortCacheSchedulerEnabled();
      Mockito.doReturn(true).when(shortCacheUtil).isClientShortCacheInitializationInProgress();
      customServiceHealthCheck.health();
   }

   @Test
   @DisplayName("Test Health Check with Exception")
   public void healthWithException() throws Exception {
      Mockito.doThrow(new RuntimeException()).when(appConfigReader).isEnableClientShortCache();
      customServiceHealthCheck.health();
   }
}

