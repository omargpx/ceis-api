package com.citse.ceis.models;

import lombok.Data;

@Data
public class GUSResponse {
    private String url;
    private String Origin;
    private Object data;
    private String status;

    public GUSResponse(String url, String origin, Object data, String status) {
        this.url = url;
        Origin = origin;
        this.data = data;
        this.status = status;
    }
}
