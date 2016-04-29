package com.hhly.mlottery.callback;

/**
 * 获取缓存信息并且访问数据连接获取最新数据的回调接口
 * <p>
 * 此回调接口用于获取缓存需要初始化界面数据，而后通过网络获取到新数据之后，重新刷新界面数据，两个方法整合到一个方法中使用
 * @ClassName: GetCacheDataCallback 
 * 
 * @param <T>
 */
public interface GetCacheDataLaterConnectionCallback<T> {
	/**
	 * 获取到缓存和请求结果之后调用事件，将请求之前和请求之后的操作集中到一个方法中
	 * @param response
	 */
	void result(T response, boolean isCacheData); 
}