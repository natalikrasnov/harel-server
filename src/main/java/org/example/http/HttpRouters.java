package org.example.http;

import com.sun.net.httpserver.HttpHandler;
import org.example.httpHandler.BaseHandler;
import org.example.httpHandler.customers.CustomerByIdHandler;
import org.example.httpHandler.customers.CustomersHandler;
import org.example.httpHandler.customers.EditCustomerHandler;
import org.example.httpHandler.login.LoginHandler;

public enum HttpRouters {

    login("/login", new LoginHandler()),
    getCustomers("/customers", new CustomersHandler()),
    getCustomer("/customer", new CustomerByIdHandler()),
    editCustomer("/edit/customer", new EditCustomerHandler());

    public final String PATH;
    public final BaseHandler HANDLER;

    HttpRouters(String PATH, BaseHandler HANDLER){
        this.PATH = PATH;
        this.HANDLER = HANDLER;
    }
}
