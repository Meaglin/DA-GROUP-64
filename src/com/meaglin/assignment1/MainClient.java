package com.meaglin.assignment1;


import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class MainClient {


    public static int nodeCount = 6;
    public static ClientRunner[] nodes;

    public static void main(String[] args) {

//        try {java.rmi.registry.LocateRegistry.createRegistry(1099);} catch (RemoteException e) {e.printStackTrace();}
//        // Create and install a security manager
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new RMISecurityManager());
//        }

        nodes = new ClientRunner[nodeCount];
        for(int i = 0; i < 3; i += 1) {
            nodes[i] = new ClientRunner(i, nodeCount);
            new Thread(nodes[i]).start();
        }
    }
}
