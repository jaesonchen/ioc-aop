package com.asiainfo.simulation.support;

import java.util.HashMap;
import java.util.Map;

public class Context {

	public static final Map<String, String> context = new HashMap<String, String>();
	
	public static String get(String key) {
		return context.get(key);
	}
	public static void put(String key, String value) {
		context.put(key, value);
	}
}
