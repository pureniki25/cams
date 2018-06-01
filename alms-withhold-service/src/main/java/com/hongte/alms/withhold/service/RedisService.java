package com.hongte.alms.withhold.service;
/**
 * 
 * @author czs
 *  2018/05/29
 */
public interface RedisService {
	public boolean set(String key, String value);  
    
    public String get(String key);  
    
    public boolean hasKey(String key);  
}
