package com.hhly.mlottery.callback;

public interface ConnectionCallback<T> {
	/**
	 * 回调结果
	* @param obj 返回接收json数据类
	 */
	public void result(T response);
}
