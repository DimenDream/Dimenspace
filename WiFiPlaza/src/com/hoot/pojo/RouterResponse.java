package com.hoot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class RouterResponse extends Response {
	private Router router;

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

	@Override
	public String toString() {
		return "RouterResponse [router=" + router + ", getHead()=" + getHead()
				+ "]";
	}

}
