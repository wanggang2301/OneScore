package com.hhly.mlottery.bean;
/**
 *
 * @ClassName: UmengInfo
 * @Description: umeng返回的终端id 和错误代码，0成功 -1失败
 * @author Tenney
 * @date 2015-11-24 下午3:12:48
 */
public class UmengInfo {
	private String TERID; // 终端ID
	private String CODE; // 错误代码，正常为0，错误为-1

	public String getTERID() {
		return TERID;
	}

	public void setTERID(String tERID) {
		TERID = tERID;
	}

	public String getCODE() {
		return CODE;
	}

	public void setCODE(String cODE) {
		CODE = cODE;
	}

}
