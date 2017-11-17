package com.meaglin;


import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Main {


    public static int nodeCount = 3;
    public static ServerRunner[] nodes;

    public static void main(String[] args) {

        try {java.rmi.registry.LocateRegistry.createRegistry(1099);} catch (RemoteException e) {e.printStackTrace();}
        // Create and install a security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        nodes = new ServerRunner[nodeCount];
        for(int i = 0; i < nodeCount; i += 1) {
            nodes[i] = new ServerRunner(i, nodeCount);
            new Thread(nodes[i]).start();
        }
    }
}
