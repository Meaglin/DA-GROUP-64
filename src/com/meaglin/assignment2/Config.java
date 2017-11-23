package com.meaglin.assignment2;

public class Config {


    String host = "127.0.0.1";
    String port = "1099";

    public String getUrl(String name) {
        return "rmi://" + host + ":" + port + "/" + name;
    }
}
