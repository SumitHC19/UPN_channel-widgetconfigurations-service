package com.aonhewitt.upoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.aonhewitt.upoint.cache.util.ShortCacheUtil;

//@Component
public class ShortCacheInitializer implements CommandLineRunner {

	@Autowired
	ShortCacheUtil shortCacheUtil;

	@Override
	public void run(String... args) throws Exception {		
		shortCacheUtil.initializeJVMShortCahe();
	}
}
