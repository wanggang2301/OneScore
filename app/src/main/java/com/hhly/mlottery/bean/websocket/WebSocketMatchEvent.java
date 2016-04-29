package com.hhly.mlottery.bean.websocket;

import java.util.Map;

public class WebSocketMatchEvent {
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
