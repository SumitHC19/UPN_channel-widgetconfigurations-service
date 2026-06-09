package com.aonhewitt.upoint.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamClass;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.ConfigurableObjectInputStream;
import org.springframework.util.ObjectUtils;

public class NewConfigurableObjectInputStream extends ConfigurableObjectInputStream {

	private static final Map<String, Class<?>> cachedClasses = new ConcurrentHashMap<>();

	public NewConfigurableObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
		super(in, classLoader);
	}

	public NewConfigurableObjectInputStream(InputStream in, ClassLoader classLoader, boolean acceptProxyClasses)
			throws IOException {
		super(in, classLoader, acceptProxyClasses);
	}

	@Override
	public Class<?> resolveClass(ObjectStreamClass classDesc) throws IOException, ClassNotFoundException {

		String name = classDesc.getName();

		Class<?> clazz = cachedClasses.get(name);

		if (ObjectUtils.isEmpty(clazz)) {
			clazz = super.resolveClass(classDesc);
			cachedClasses.put(name, clazz);
		}

		return clazz;
	}
}
