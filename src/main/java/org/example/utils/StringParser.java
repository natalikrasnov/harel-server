package org.example.utils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class StringParser {

    public static String readInputStream(InputStream inputStream){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while(true) {
            try {
                if ((i = inputStream.read()) == -1) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append((char) i);
        }
        return sb.toString();
    }

    public static JSONObject getQueryFromString(String queryS){
        if(queryS == null || queryS.isEmpty()) return null;
        JSONObject query = new JSONObject();
        for(String q : queryS.split("&")){
            String[] keyValue = q.split("=");
            query.put(keyValue[0], keyValue[1]);
        }
        return query;
    }
}
