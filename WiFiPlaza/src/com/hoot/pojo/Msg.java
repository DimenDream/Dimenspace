package com.hoot.pojo;

public class Msg {
	private long id;
	private int code;
	private String msg;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Msg [id=" + id + ", code=" + code + ", msg=" + msg + "]";
	}

}
