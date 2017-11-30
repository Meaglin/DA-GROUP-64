package com.meaglin.assignment3;

public class Slave extends Server {
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
