package com.meaglin;


import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) {

        try {java.rmi.registry.LocateRegistry.createRegistry(1099);} catch (RemoteException e) {e.printStackTrace();}
        // Create and install a security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        ServerRunner server = new ServerRunner();
        new Thread(server).start();

//        startClient();

    }

    public static void startClient() {
        ClientRunner client = new ClientRunner();
        new Thread(client).start();
    }
}
