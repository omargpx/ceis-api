package com.citse.ceis.models;

import lombok.Data;

@Data
public class InfoResponse {
    private String url;
    private String message;
    private Object detail;

    public InfoResponse(String url, String message, Object detail) {
        this.url = url;
        this.message = message;
        this.detail = detail;
    }
}