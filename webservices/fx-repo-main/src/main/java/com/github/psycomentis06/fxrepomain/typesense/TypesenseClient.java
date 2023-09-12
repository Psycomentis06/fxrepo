package com.github.psycomentis06.fxrepomain.typesense;

import org.typesense.api.Client;
import org.typesense.api.Configuration;

public class TypesenseClient extends Client {
    public TypesenseClient(Configuration configuration) {
        super(configuration);
    }
}
