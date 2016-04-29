package com.hhly.mlottery.bean;

public class HomeBanner  {
	private String  picUrl;
	private int jumpType;
	private String operation;
	
	
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public int getJumpType() {
		return jumpType;
	}
	public void setJumpType(int jumpType) {
		this.jumpType = jumpType;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	@Override
	public String toString() {
		return "HomeBanner [picUrl=" + picUrl + ", jumpType=" + jumpType + ", operation=" + operation + "]";
	}

}
