package com.citse.ceis.models;

import lombok.Getter;

@Getter
public enum EntityType {
    UPeU(1,"Universidad Peruana Unión"),
    UCV(2,"Universidad Cesar Vallejo"),
    UNSM(3,"Universidad Nacional de San Martín"),
    OTHER(4, "Otra institución");

    private final int order;
    private final String description;

    EntityType(int order, String description){
        this.order= order;
        this.description = description;
    }
}
