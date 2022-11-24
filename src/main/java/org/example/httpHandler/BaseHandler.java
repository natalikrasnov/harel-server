package org.example.httpHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

import org.example.http.HttpResponseCode;
import org.example.utils.FileReader;
import org.json.*;


public class BaseHandler implements HttpHandler {

    private JSONObject responseData = new JSONObject();
    private HttpResponseCode httpResponseCode;

    protected void setResponseDataAndCode(JSONObject data, HttpResponseCode responseCode){
        this.responseData = data;
        this.httpResponseCode = responseCode;
    }

    protected JSONObject getJsonObFromFile(String fileName) throws IOException {
        String fileContent = FileReader.readFromFile(fileName);
        return XML.toJSONObject(fileContent);
    }

    protected void saveNewJsonToXmlFile(String fileName, JSONObject newJsonData) throws IOException {
        String newXmlData = XML.toString(newJsonData);
        FileReader.writeToFile(fileName, newXmlData);
    }

    protected void addCors(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            exchange.sendResponseHeaders(204, -1);
            return;
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = responseData.toString();
        exchange.sendResponseHeaders(httpResponseCode.RESPONSE_CODE, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
