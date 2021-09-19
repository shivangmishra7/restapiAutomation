package com.restAssured.auth;

public class GetSecurityToken {

	
 private String token;
	
	public String getToken() {
		/* System.out.println("inside Getter:" + this.token); */
		return token;
	}

	public void setToken(String token) {
		this.token = token;
		/* System.out.println(token); */
	}
}
