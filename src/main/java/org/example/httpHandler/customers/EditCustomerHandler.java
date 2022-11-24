package org.example.httpHandler.customers;

import com.sun.net.httpserver.HttpExchange;
import org.example.http.HttpResponseCode;
import org.example.httpHandler.BaseHandler;
import org.example.utils.StringParser;
import org.example.utils.Validator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class EditCustomerHandler extends BaseHandler {

    private final String filePath = "src/main/resources/Customers.txt";
    JSONObject errorResponseBody = new JSONObject();
    private String email;
    private String id;

    private String bankAccount;
    private String name;
    private String lastName;

    private void setInvalidResponse(){
        errorResponseBody.put("message", "Invalid Input Data");
        super.setResponseDataAndCode(errorResponseBody, HttpResponseCode.BAD_REQUEST);
    }
    private boolean getBodyParams( InputStream requestBody ){

        String requestBodyText = StringParser.readInputStream(requestBody);
        if(requestBodyText.isEmpty()) return false;

        JSONObject reqBodyJson = new JSONObject(requestBodyText);

        this.id = reqBodyJson.optString("id");
        this.email = reqBodyJson.optString("email");
        this.bankAccount = reqBodyJson.optString("bankAccount");
        this.name = reqBodyJson.optString("name");
        this.lastName = reqBodyJson.optString("lastName");

        if(!checkRequestValidation()){
            setInvalidResponse();
            return false;
        }
        return true;
    }

    private boolean checkRequestValidation(){
        return (email.equals("") || Validator.isEmailValid(email)) && !id.equals("");
    }

    private JSONObject parseCustomer(HashMap customerObj){
        JSONObject customer = new JSONObject();
        customer.put("id",customerObj.get("id"));
        customer.put("email",customerObj.get("email"));
        customer.put("name",customerObj.get("name"));
        customer.put("lastName",customerObj.get("lastName"));
        customer.put("bankAccount",customerObj.get("bankAccount"));
        customer.put("date",customerObj.get("date"));
        return customer;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            super.addCors(exchange);

            if(!getBodyParams(exchange.getRequestBody())) return;

            JSONObject customersJsonData = super.getJsonObFromFile(filePath);
            JSONArray allCustomers =  (customersJsonData.optJSONObject("customersData")).optJSONArray("customers");

            JSONObject currentCustomer = null;
            JSONObject newCustomersObject = new JSONObject();
            JSONObject newCustomers = new JSONObject();
            JSONArray newCustomersArray = new JSONArray();

            for(Object customerObj : allCustomers.toList()){
                JSONObject newCustomer = parseCustomer((HashMap) customerObj);
                if(newCustomer.get("id").toString().equals(id)){
                    if(!Objects.equals(email, "")) newCustomer.put("email", email);
                    if(!Objects.equals(bankAccount, ""))  newCustomer.put("bankAccount", bankAccount);
                    if(!Objects.equals(name, ""))  newCustomer.put("name", name);
                    if(!Objects.equals(lastName, ""))  newCustomer.put("lastName", lastName);
                    currentCustomer = newCustomer;
                }
                newCustomersArray.put(newCustomer);
            }
            newCustomers.put("customers",newCustomersArray );
            newCustomersObject.put("customersData", newCustomers);

            if(currentCustomer == null) setInvalidResponse();
            else {
                super.setResponseDataAndCode(currentCustomer,HttpResponseCode.SUCCESS);
                super.saveNewJsonToXmlFile(filePath, newCustomersObject);
            }
        }catch (Exception e){
            e.printStackTrace();
            errorResponseBody.put("message", e.getMessage());
            super.setResponseDataAndCode(errorResponseBody,HttpResponseCode.INTERNAL_SERVER_ERROR);
        }finally {
            super.handle(exchange);
        }
    }
}
