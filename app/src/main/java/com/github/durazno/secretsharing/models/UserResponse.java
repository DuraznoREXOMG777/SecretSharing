package com.github.durazno.secretsharing.models;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("email_verified_at")
    String email_verified_at;

    @SerializedName("created_at")
    String created_at;

    @SerializedName("updated_at")
    String updated_at;

    public UserResponse(int id, String name, String email_verified_at, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.email_verified_at = email_verified_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
