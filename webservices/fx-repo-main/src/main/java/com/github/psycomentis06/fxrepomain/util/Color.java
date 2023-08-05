package com.github.psycomentis06.fxrepomain.util;

import java.util.regex.Pattern;

public class Color {
    public static boolean isRgba(String color) {
        String ns = color.replace(" ", "");
        if (ns.startsWith("rgba(")) {
            int r, g, b;
            float a;
            ns = ns.substring(4, ns.indexOf(")"));
            ns = ns
                    .replace("(", "")
                    .replace(")", "");
            String[]  ar = ns.split(",");
            r =  Integer.parseInt(ar[0]);
            g = Integer.parseInt(ar[1]);
            b = Integer.parseInt(ar[2]);
            a =  Float.parseFloat(ar[3]);
            return  a >= 0.0 && a <= 1.0 && r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255;
        }
        return false;
    }
}
