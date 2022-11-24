package org.example.httpHandler.customers;

import com.sun.net.httpserver.HttpExchange;
import org.example.http.HttpResponseCode;
import org.example.httpHandler.BaseHandler;
import org.json.JSONObject;

import java.io.IOException;

public class CustomersHandler extends BaseHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            super.addCors(exchange);

            JSONObject customersJsonData = super.getJsonObFromFile("src/main/resources/Customers.txt");
            JSONObject customers =  customersJsonData.optJSONObject("customersData");
            super.setResponseDataAndCode(customers,HttpResponseCode.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            JSONObject errorResponseBody = new JSONObject();
            errorResponseBody.put("message", e.getMessage());
            super.setResponseDataAndCode(errorResponseBody,HttpResponseCode.INTERNAL_SERVER_ERROR);
        }finally {
            super.handle(exchange);
        }
    }
}
