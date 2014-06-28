package com.hoot.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonInclude(Include.NON_DEFAULT)
public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Msg head;

	public Msg getHead() {
		return head;
	}

	public void setHead(Msg head) {
		this.head = head;
	}

	@Override
	public String toString() {
		return "Response [head=" + head + "]";
	}

}
