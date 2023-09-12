package com.github.psycomentis06.fxrepomain.typesense;

import org.typesense.api.Configuration;
import org.typesense.resources.Node;

import java.time.Duration;
import java.util.List;

public class TypesenseConfiguration extends Configuration {
    public TypesenseConfiguration(List<TypesenseNode> nodes, Duration connectionTimeout, String apiKey) {
        super(nodes.stream().map(TypesenseNode::toNode).toList(), connectionTimeout, apiKey);
    }

    public TypesenseConfiguration(List<TypesenseNode> nodes, Duration connectionTimeout, Duration readTimeout, String apiKey) {
        super(nodes.stream().map(TypesenseNode::toNode).toList(), connectionTimeout, readTimeout, apiKey);
    }

    public TypesenseConfiguration(TypesenseNode nearestNode, List<TypesenseNode> nodes, Duration connectionTimeout, String apiKey) {
        super(nearestNode.toNode(), nodes.stream().map(TypesenseNode::toNode).toList(), connectionTimeout, apiKey);
    }
}
