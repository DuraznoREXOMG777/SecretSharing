package com.github.durazno.secretsharing.models;

import com.google.gson.annotations.SerializedName;

public class KeyResponse {

    @SerializedName("response")
    String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public KeyResponse(String response) {
        this.response = response;
    }
}
