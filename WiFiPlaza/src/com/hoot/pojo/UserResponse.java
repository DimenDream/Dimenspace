package com.hoot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class UserResponse extends Response {

	private User user;
	private String session;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
}
