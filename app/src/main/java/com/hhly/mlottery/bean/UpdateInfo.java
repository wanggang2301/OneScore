package com.hhly.mlottery.bean;

public class UpdateInfo {

	private String version;   //服务器上的版本号
	private String description;  //升级信息
	private String url;    //apk下载地址
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	
	
	
}
