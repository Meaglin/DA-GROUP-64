package com.meaglin;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public Client() {
    }


    public void query() {
        try {
//            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            Hello stub = (Hello) Naming.lookup("hello");
            String response = stub.sayHello();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}