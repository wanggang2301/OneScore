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


	/**
     * @author: Wangg
     * @Name：ForeignInfomationEvent
     * @Description:
     * @Created on:2016/9/23  15:10.
     */

    public static class ForeignInfomationEvent {


        public ForeignInfomationEvent(int id, int favroite) {
            this.id = id;
            this.favroite = favroite;
        }

        private int id;

        private int favroite;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFavroite() {
            return favroite;
        }

        public void setFavroite(int favroite) {
            this.favroite = favroite;
        }
    }
}
