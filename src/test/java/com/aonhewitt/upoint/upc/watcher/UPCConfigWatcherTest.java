package com.aonhewitt.upoint.upc.watcher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aonhewitt.upoint.cache.util.AppConfigReader;
import com.aonhewitt.upoint.util.ConfigurationListUtils;


//@ExtendWith(MockitoExtension.class)
public class UPCConfigWatcherTest {
      

   @InjectMocks
   UPCConfigWatcher uPCConfigWatcher;
 
   @Mock
   private AppConfigReader appConfigReader;

   @Mock
   private ConfigurationListUtils configurationListUtils;

   @AfterEach
   public void clear() {
      Mockito.clearAllCaches();
   }
   
   @BeforeEach
   public void setup() throws Exception {
      MockitoAnnotations.openMocks(this);
   }

   @Test
   public void test() {
      Mockito.mockStatic(AppConfigReader.class);
      Mockito.when(AppConfigReader.getInstance()).thenReturn(appConfigReader);
      Mockito.when(appConfigReader.isUpcJVMCacheClear()).thenReturn(true);
      uPCConfigWatcher.UpdateUpcConfigCache();;
   }

}

