package com.github.durazno.secretsharing.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("access_token")
    String accessToken;

    @SerializedName("token_type")
    String tokenType;

    @SerializedName("expires_at")
    String expirationDate;

    public AuthResponse(String accessToken, String tokenType, String expirationDate) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expirationDate = expirationDate;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
}
