package com.meaglin.assignment2;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Client {

    public static int nodeCount = 6;
    public static NodeRunner[] nodes;

    public static void main(String[] args) throws RemoteException, InterruptedException {
        setupServer();

        Config config = new Config();
        config.nodeCount = 3;
        config.remotehost = "127.0.0.1";
        config.isServer = false;

        nodes = new NodeRunner[nodeCount];
        for(int i = (nodeCount/2); i < nodeCount; i += 1) {
            nodes[i] = new NodeRunner(i, nodeCount, config);
        }

        for(int i = (nodeCount/2); i < nodeCount; i += 1) {
            new Thread(nodes[i]).start();
        }
    }

    public static void setupServer() {
//        System.setProperty("java.security.policy","file:///Users/verburg/IdeaProjects/DAAssignment1/my.policy");

        try {java.rmi.registry.LocateRegistry.createRegistry(1099);} catch (RemoteException e) {e.printStackTrace();}
        // Create and install a security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
    }
}
