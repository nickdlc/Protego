package com.example.protego.web;

import android.net.Uri;
import java.util.Map;

@Deprecated
public class UriFormatter {
    private static String urlScheme = "http";

    // prefix ex: 127.0.0.1:8000
    public static String formatUrl(String prefix, Endpoint endpoint, Map<String, String> paramList) {
        String endpointPath = endpoint.getEndpointPath();
        // Build url format `http://127.0.0.1:8000/path`
        Uri.Builder builder = new Uri.Builder()
                                    .encodedAuthority(prefix)
                                    .scheme(urlScheme)
                                    .appendPath(endpointPath);

        if (paramList != null) {
            // Add url params to endpoint, ex. `http://127.0.0.1:8000/path?param1=val1&param2=val2...`
            for (Map.Entry<String, String> param : paramList.entrySet()) {
                String paramKey = param.getKey();
                String paramVal = param.getValue();

                builder.appendQueryParameter(paramKey, paramVal);
            }
        }

        return builder.build().toString();
    }

    public static String formatUrl(Endpoint endpoint, Map<String, String> paramList) {
        return formatUrl(RequestManager.prefixURL, endpoint, paramList);
    }

    public static String formatUrl(String prefix, Endpoint endpoint) {
        return formatUrl(prefix, endpoint, null);
    }

    public static String formatUrl(Endpoint endpoint) {
        return formatUrl(RequestManager.prefixURL, endpoint, null);
    }
}
