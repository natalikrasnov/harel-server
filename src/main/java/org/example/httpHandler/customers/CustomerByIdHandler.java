package org.example.httpHandler.customers;

import com.sun.net.httpserver.HttpExchange;
import org.example.http.HttpResponseCode;
import org.example.httpHandler.BaseHandler;
import org.example.utils.StringParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class CustomerByIdHandler extends BaseHandler {

    JSONObject errorResponseBody = new JSONObject();
    private String id;

    private void setInvalidResponse(){
        errorResponseBody.put("message", "Customer not found");
        super.setResponseDataAndCode(errorResponseBody, HttpResponseCode.NOT_FOUND);
    }
    private boolean getParams( HttpExchange request ){
        JSONObject query = StringParser
                .getQueryFromString(
                        request.getRequestURI().getQuery()
                );
        if (query != null) id = query.optString("id");
        return query !=null && id != null && !id.isEmpty();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            super.addCors(exchange);

            if(!getParams(exchange)) return;

            JSONObject customersJsonData = super.getJsonObFromFile("src/main/resources/Customers.txt");
            JSONArray allCustomers =  (customersJsonData.optJSONObject("customersData")).optJSONArray("customers");
            JSONObject currentCustomer=null;
            for(Object customerObj : allCustomers.toList()){
                HashMap customer = (HashMap) customerObj;
                if(customer.get("id").toString().equals(id)){
                    currentCustomer = new JSONObject(customer);
                    break;
                }
            }
            if(currentCustomer == null) setInvalidResponse();
            else super.setResponseDataAndCode(currentCustomer,HttpResponseCode.SUCCESS);

        }catch (Exception e){
            e.printStackTrace();
            errorResponseBody.put("message", e.getMessage());
            super.setResponseDataAndCode(errorResponseBody,HttpResponseCode.INTERNAL_SERVER_ERROR);
        }finally {
            super.handle(exchange);
        }
    }
}
