package com.natasha.clockio.model;

import com.google.gson.annotations.SerializedName;

public class Test {
    @SerializedName("test")
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
