package com.meaglin.assignment3;


import java.util.ArrayList;
import java.util.List;

public class Master extends Server {

    static List<Node> nodes = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        String myIp = determineIP();
        if (myIp == null) {
            System.out.println("Cannot determine ip, exiting");
            return;
        }
        System.out.println("My IP: " + myIp);
        setupServer();

    }


}
