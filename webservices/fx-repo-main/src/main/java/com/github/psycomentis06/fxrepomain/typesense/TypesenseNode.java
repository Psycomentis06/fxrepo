package com.github.psycomentis06.fxrepomain.typesense;

import org.typesense.resources.Node;

public class TypesenseNode extends Node {
    public TypesenseNode(String protocol, String host, String port) {
        super(protocol, host, port);
    }

    public Node toNode() {
        return this;
    }
}
