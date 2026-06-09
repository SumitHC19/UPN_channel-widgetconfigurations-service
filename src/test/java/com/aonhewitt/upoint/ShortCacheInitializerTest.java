package com.aonhewitt.upoint;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aonhewitt.upoint.cache.util.ShortCacheUtil;

@ExtendWith(MockitoExtension.class)
public class ShortCacheInitializerTest {
@InjectMocks
private ShortCacheInitializer shortInit;
@Mock
private ShortCacheUtil shortCacheUtil;

@Test
public void testRun() throws Exception {
   MockitoAnnotations.openMocks(this);
   shortInit.run();
   }
}
