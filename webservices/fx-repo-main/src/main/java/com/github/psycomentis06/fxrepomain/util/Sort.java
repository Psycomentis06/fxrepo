package com.github.psycomentis06.fxrepomain.util;

import java.lang.reflect.Field;

public class Sort {
    public static org.springframework.data.domain.Sort.Direction getSortDirection(String direction) {
        org.springframework.data.domain.Sort.Direction dir;
        switch (direction.toUpperCase()) {
            case "DESC", "DOWN", "DESCENDING" -> dir = org.springframework.data.domain.Sort.Direction.DESC;
            default -> dir = org.springframework.data.domain.Sort.Direction.ASC;
        }
        return dir;
    }

    public static String getSortAttributeName(Class<?> tClass, String name, String defaultName) {
        Field[] fields = tClass.getDeclaredFields();
        for (Field f :
                fields) {
            if (f.getName().equals(name)) return name;
        }
        return defaultName;
    }
}
