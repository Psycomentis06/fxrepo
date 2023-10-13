package com.github.psycomentis06.fxrepomain.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class IntegerDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JacksonException {
        String value = p.getValueAsString();
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
