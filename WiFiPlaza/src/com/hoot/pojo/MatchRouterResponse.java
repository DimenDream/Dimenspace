package com.hoot.pojo;

import java.util.List;

public class MatchRouterResponse extends Response {
	private List<Router> routers;

	public List<Router> getRouters() {
		return routers;
	}

	public void setRouters(List<Router> routers) {
		this.routers = routers;
	}

	@Override
	public String toString() {
		return "MatchRouterResponse [routers=" + routers + ", toString()="
				+ super.toString() + "]";
	}

}
