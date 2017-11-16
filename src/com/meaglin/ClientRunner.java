package com.meaglin;

public class ClientRunner implements Runnable {

    public void run() {
        try {
            Thread.sleep(50);
            Client client = new Client();
            client.query();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
