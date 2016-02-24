package com.baoluo.community.entity;

/**
 * api 令牌 实体
 * 
 * @author Ryan_Fu 2015-6-1
 */
public class TokenInfo {

	private String access_token;
	private String token_type;
	private String expires_in;
	private String refresh_token;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public TokenInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TokenInfo(String access_token, String token_type, String expires_in,
			String refresh_token) {
		super();
		this.access_token = access_token;
		this.token_type = token_type;
		this.expires_in = expires_in;
		this.refresh_token = refresh_token;
	}

}
