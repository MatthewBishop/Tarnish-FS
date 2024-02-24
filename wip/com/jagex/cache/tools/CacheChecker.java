package com.jagex.cache.tools;

import com.jagex.cache.Cache;

public class CacheChecker {

	public static void main(String[] args) throws Exception {
		Cache cache = new Cache("C:\\Users\\Home\\Downloads\\Tarnish Package\\cache\\");
		byte[] data = cache.getFile(0, 1);//22987
		
		System.out.println(data.length);
		//cache.check();
	}

}