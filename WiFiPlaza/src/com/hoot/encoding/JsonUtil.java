package com.hoot.encoding;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static ObjectMapper mObjMapper;

	public static synchronized ObjectMapper getMapper() {
		if (mObjMapper == null) {
			mObjMapper = new ObjectMapper();
			mObjMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		return mObjMapper;
	}

	public static byte[] getBytes(Object object) {
		try {
			return getMapper().writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getString(Object object) {
		try {
			return getMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getObject(InputStream in, Class<T> clazz) {
		try {
			return getMapper().readValue(in, clazz);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getObject(String rawData, Class<T> clazz) {
		try {
			return getMapper().readValue(rawData, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List getObject(String str, JavaType type) {
		// getMapper().readv
		try {
			return getMapper().readValue(str, type);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static JavaType getCollectionType(Class<?> collectionClass,
			Class<?>... elementClasses) {
		return getMapper().getTypeFactory().constructParametricType(
				collectionClass, elementClasses);
	}
}
