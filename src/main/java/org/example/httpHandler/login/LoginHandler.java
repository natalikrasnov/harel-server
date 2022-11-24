package org.example.httpHandler.login;

import com.sun.net.httpserver.HttpExchange;
import org.example.http.HttpResponseCode;
import org.example.httpHandler.BaseHandler;
import org.example.utils.StringParser;
import org.example.utils.Validator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LoginHandler extends BaseHandler {

    JSONObject errorResponseBody = new JSONObject();
    private String email;
    private String password;
    private String name;
    private String lastName;

    private void setInvalidResponse(){
        errorResponseBody.put("message", "Invalid Login Data");
        super.setResponseDataAndCode(errorResponseBody,HttpResponseCode.UNAUTHENTICATED);
    }
    private boolean getBodyParams(InputStream requestBody){
        String requestBodyText = StringParser.readInputStream(requestBody);
        if(requestBodyText.isEmpty()) return false;
        JSONObject reqBodyJson = new JSONObject(requestBodyText);

        this.email = reqBodyJson.optString("email");
        this.password = reqBodyJson.optString("password");
        this.name = reqBodyJson.optString("name");
        this.lastName = reqBodyJson.optString("lastName");

        if(!checkRequestValidation()){
            setInvalidResponse();
            return false;
        }
        return true;
    }

    private boolean checkRequestValidation(){
        return email != null && !email.equals("") && Validator.isEmailValid(email)
                && password != null && !password.equals("") && Validator.isPasswordValid(password);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            super.addCors(exchange);

            if(!getBodyParams(exchange.getRequestBody())) return;

            JSONObject usersJsonData = super.getJsonObFromFile("src/main/resources/Users.txt");
            JSONArray allUsers =  (usersJsonData.optJSONObject("usersData")).optJSONArray("users");
            JSONObject loginUser=null;
            for(Object userObj : allUsers.toList()){
                HashMap user = (HashMap) userObj;
                String userEmail = user.get("email").toString();
                String userPassword= user.get("password").toString();
                if(userEmail.equals(email) && userPassword.equals(password)){
                    loginUser = new JSONObject(user);
                    break;
                }
            }
            if(loginUser == null) setInvalidResponse();
            else super.setResponseDataAndCode(loginUser,HttpResponseCode.SUCCESS);

        }catch (Exception e){
            e.printStackTrace();
            errorResponseBody.put("message", e.getMessage());
            super.setResponseDataAndCode(errorResponseBody,HttpResponseCode.INTERNAL_SERVER_ERROR);
        }finally {
            super.handle(exchange);
        }
    }
}
