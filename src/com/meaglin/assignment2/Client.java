package com.meaglin.assignment2;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

/**
 * Main class for the client.
 */
public class Client {

    public static int nodeCount = 6;    // Total number of nodes.
    public static NodeRunner[] nodes;   // List of NodeRunners.

    /**
     * The main class setups a server and then runs the second half of
     * the nodes on this machine by starting them in separate threads.
     * @param args                      ?
     * @throws RemoteException          Remote Exception.
     * @throws InterruptedException     Interrupted Exception.
     */
    public static void main(String[] args) throws RemoteException, InterruptedException {
        setupServer();

        Config config = new Config();           // Create new server configuration.
        config.nodeCount = nodeCount;
        config.remotehost = "145.94.229.202";   // IP address of the remote host.
        config.isServer = false;

        nodes = new NodeRunner[nodeCount];
        for(int i = (nodeCount/2); i < nodeCount; i += 1) {
            nodes[i] = new NodeRunner(i, nodeCount, config);
        }
        for(int i = (nodeCount/2); i < nodeCount; i += 1) {
            new Thread(nodes[i]).start();
        }
    }

    /**
     *  Setups the server by creating a local registry and applies security measures.
     */
    public static void setupServer() {
        System.setProperty("java.security.policy","file:///Users/verburg/IdeaProjects/DAAssignment1/my.policy");
        System.setProperty("java.rmi.server.hostname", "145.94.174.113");

        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager()); // Create and install a security manager.
        }
    }
}