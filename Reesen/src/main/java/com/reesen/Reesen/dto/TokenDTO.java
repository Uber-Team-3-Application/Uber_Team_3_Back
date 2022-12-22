package com.reesen.Reesen.dto;

public class TokenDTO {
    private String accessToken;

    public TokenDTO() {}

    public TokenDTO(String accessToken) {
        this.accessToken = accessToken;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
