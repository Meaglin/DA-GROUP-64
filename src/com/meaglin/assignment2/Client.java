package com.meaglin.assignment2;

import java.rmi.RemoteException;

public class Client {

    public static int nodeCount = 6;
    public static int startCount = 3;
    public static NodeRunner[] nodes;

    public static void main(String[] args) throws RemoteException, InterruptedException {
        Config config = new Config();
        config.host = "127.0.0.1";
        nodes = new NodeRunner[nodeCount];
        for(int i = startCount; i < nodeCount; i += 1) {
            nodes[i] = new NodeRunner(i, nodeCount, config);
        }

        for(int i = startCount; i < nodeCount; i += 1) {
            new Thread(nodes[i]).start();
        }
    }
}
