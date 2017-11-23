package com.meaglin.assignment2;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Server {

    public static int nodeCount = 6;
    public static int stopCount = 3;
    public static NodeRunner[] nodes;

    public static void main(String[] args) throws RemoteException, InterruptedException {
        setupServer();


        Config config = new Config();
        nodes = new NodeRunner[nodeCount];
        for(int i = 0; i < stopCount; i += 1) {
            nodes[i] = new NodeRunner(i, nodeCount, config);
        }
        Thread.sleep(30000);
        for(int i = 0; i < stopCount; i += 1) {
            new Thread(nodes[i]).start();
        }
    }

    public static void setupServer() {
        try {java.rmi.registry.LocateRegistry.createRegistry(1099);} catch (RemoteException e) {e.printStackTrace();}
        // Create and install a security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
    }
}
