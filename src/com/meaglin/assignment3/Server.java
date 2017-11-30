package com.meaglin.assignment3;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

public class Server extends UnicastRemoteObject {

    public Server() throws RemoteException {
    }

    public static String determineIP() throws SocketException {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                String address = i.getHostAddress();
                if (address.contains(":")) { // Ipv6 is meh
                    continue;
                }
                if (address.startsWith("127.")) { // No localhost
                    continue;
                }
//                if (address.startsWith("10.0") || address.startsWith("192.168")) { // No lan address
//                    continue;;
//                }
                return address;
            }
        }
        return null;
    }

    public static void setupServer() throws SocketException {
//        System.setProperty("java.security.policy","file:///Users/verburg/IdeaProjects/DAAssignment1/my.policy");
        System.setProperty("java.rmi.server.hostname", determineIP());
        try {java.rmi.registry.LocateRegistry.createRegistry(1099);} catch (RemoteException e) {e.printStackTrace();}
        // Create and install a security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
    }
}
