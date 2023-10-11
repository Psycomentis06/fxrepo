package com.github.psycomentis06.fxrepomain.model.kafka;

public enum Status {
    DONE("DONE"),
    CANCELED("CANCELED"),
    FAILED("FAILED"),
    IN_PROGRESS("IN_PROGRESS"),
    ;

    Status(String status) {
    }
}
