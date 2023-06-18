package attributes;

import java.io.Serializable;
import users.*;

public class Request implements Serializable
{
	private static final long serialVersionUID = -3068396638338390061L;

	private User from;
	private User to;
	
	private String requestType;
	private String requestMess;
	
	public Request(User from, User to, String requestType, String requestMess) {
		this.from = from; this.to = to;
		this.requestType = requestType;
		this.requestMess = requestMess;
	}

	public User getFrom() {
		return from;
	}
	public void setFrom(User from) {
		this.from = from;
	}
	public User getTo() {
		return to;
	}
	public void setTo(User to) {
		this.to = to;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getRequestMess() {
		return requestMess;
	}
	public void setRequestMess(String requestMess) {
		this.requestMess = requestMess;
	}
	
	public String toString() {
		return "From: " + this.from.getId() + " -> to: " + this.to.getId() + ". " + this.requestType + ", " + this.requestMess;
	}
}