package com.hhly.mlottery.util;


import android.util.Log;

import java.util.HashMap;
import java.util.Map;
/**
 * @ClassName: DataBus 
 * @Description:
 * @author Tenney
 * @date 2015-10-15 上午10:35:16
 */
public class DataBus {
	
	private static final String TAG=DataBus.class.getSimpleName();
	
	
	public static final String PHOTOS_PUBLISHING="photos_publishing";
	
	public static final String PHOTOS_RETURN="photos_return";
	
	public static final String ALBUM="album";
	
	private DataBus(){}
	
	private Map<String,Object> cacheInLifecycle;
	
	private static final DataBus dataBus=new DataBus();
	
	/**
	 * 必须在application的onCreate中调用以初始化
	 */
	public static void onCreate(){
		dataBus.cacheInLifecycle=new HashMap<String, Object>();				
	}

	public static void put(String key,Object data){
		dataBus.cacheInLifecycle.put(key, data);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T get(String key){
		return (T) dataBus.cacheInLifecycle.get(key);		
	}
	
	public static void remove(String key){
		Object obj=dataBus.cacheInLifecycle.remove(key);
		if(obj!=null){
			Log.d(TAG, "成功移除数据key="+key);
		}else{
			Log.e(TAG, "找不到key="+key+"的数据！");
		}
	}
	
	public static void clearData(){
		if(dataBus.cacheInLifecycle!=null)
			dataBus.cacheInLifecycle.clear();
	}
		
	/**
	 * 必须在application的onTerminate中调用
	 */
	public static void onTerminate(){
		if(dataBus.cacheInLifecycle!=null){
			dataBus.cacheInLifecycle.clear();
			dataBus.cacheInLifecycle=null;
		}



	}
	
}
