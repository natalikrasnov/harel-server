package org.example.http;

import org.json.JSONObject;

public enum HttpResponseCode {
    SUCCESS(200),
    INTERNAL_SERVER_ERROR(500),
    NOT_FOUND(404),
    BAD_REQUEST(400),

    UNAUTHENTICATED(401);
    public final int RESPONSE_CODE;

    HttpResponseCode(int RESPONSE_CODE){
        this.RESPONSE_CODE = RESPONSE_CODE;
    }
}
