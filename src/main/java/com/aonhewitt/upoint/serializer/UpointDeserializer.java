package com.aonhewitt.upoint.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.springframework.core.ConfigurableObjectInputStream;
import java.io.IOException;
import org.springframework.core.serializer.Deserializer;

public class UpointDeserializer implements Deserializer<Object> {
	
	private final ClassLoader classLoader;


	/**
	 * Create a {@code DefaultDeserializer} with default {@link ObjectInputStream}
	 * configuration, using the "latest user-defined ClassLoader".
	 */
	public UpointDeserializer() {
		this.classLoader = null;
	}

	/**
	 * Create a {@code DefaultDeserializer} for using an {@link ObjectInputStream}
	 * with the given {@code ClassLoader}.
	 * @since 4.2.1
	 * @see ConfigurableObjectInputStream#ConfigurableObjectInputStream(InputStream, ClassLoader)
	 */
	public UpointDeserializer(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}


	/**
	 * Read from the supplied {@code InputStream} and deserialize the contents
	 * into an object.
	 * @see ObjectInputStream#readObject()
	 */
	@Override
	@SuppressWarnings("resource")
	public Object deserialize(InputStream inputStream) throws IOException {
		try {
			ObjectInputStream objectInputStream = new NewConfigurableObjectInputStream(inputStream, this.classLoader);
			return objectInputStream.readObject();
		}
		catch (Exception ex) {
			throw new IOException("Failed to deserialize object type", ex);
		}
	}
}
