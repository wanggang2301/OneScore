package com.hhly.mlottery.bean.websocket;

import java.util.List;
import java.util.Map;

public class WebSocketMatchOdd {
	private String type;
	private List<Map<String,String>> data;
	private String thirdId;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getThirdId() {
		return thirdId;
	}
	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}
	public List<Map<String, String>> getData() {
		return data;
	}
	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}
	
	
}
