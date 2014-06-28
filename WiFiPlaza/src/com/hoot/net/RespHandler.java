package com.hoot.net;


import org.apache.http.HttpResponse;

public interface RespHandler {
	void handle(HttpResponse response);
}
