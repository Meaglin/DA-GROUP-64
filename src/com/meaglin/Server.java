package com.meaglin;


import java.io.Serializable;

public class Server implements Hello, Serializable {
    private static final long serialVersionUID = 7526471155622776147L;

    public Server() {}

    public String sayHello() {
        return "Hello, world!";
    }
}
