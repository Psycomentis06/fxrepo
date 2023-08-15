package com.github.psycomentis06.fxrepomain.model.kafka;

public enum Status {
    DONE("Done"),
    CANCELED("Canceled"),
    FAILED("Failed"),
    IN_PROGRESS("In-progress"),
    ;

    Status(String status) {
    }
}
