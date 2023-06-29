package com.github.psycomentis06.fxrepomain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
@Data
public class StorageProperties {
    private String dir = "/tmp/fx-repo/main/uploads";
}
