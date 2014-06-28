package com.hoot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class Router extends Request {
	private int id;
	private String ssid;
	private String bssid;// 登陆是根据ssid，但是识别得根据mac地址，也就是bssid
	private String passwd;
	private String security;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	@Override
	public String toString() {
		return "Router [id=" + id + ", ssid=" + bssid + ", passwd=" + passwd
				+ "]";
	}
}
