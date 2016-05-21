package es.udc.csai.rest.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTToken {
	private String tokenId;

	public JWTToken(String tokenId) {
		this.tokenId = tokenId;
	}

	@JsonProperty("token_id")
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
}
