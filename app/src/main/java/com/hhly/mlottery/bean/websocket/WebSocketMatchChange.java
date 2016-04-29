package com.hhly.mlottery.bean.websocket;

import java.util.Map;

/**
 * 
 * @ClassName: WebSocketMatchChange 
 * @Description: 场次变化
 * @author chenml
 * @date 2015-10-24 下午9:40:37
 */
public class WebSocketMatchChange {
	
	private String type;
	private Map<String, String> data;

	private String thirdId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}
	
	
	
}
