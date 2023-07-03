package com.github.psycomentis06.fxrepomain.util;

import jakarta.servlet.http.HttpServletRequest;

public class Http {
    public static String getHostUrl(HttpServletRequest req) {
        var url = req.getRequestURL();
        return url.toString().replace(req.getRequestURI(), "");
    }
}
