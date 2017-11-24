package com.meaglin.assignment2;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Runs a distributed node.
 */
public class NodeRunner implements Runnable {

    DA_Peterson iface;

    /**
     * Constructs a NodeRunner by creating a new interface and binding
     * the URL of this interface to the local registry.
     * @param nodeId            Unique ID of the node.
     * @param nodeCount         Total number of nodes.
     * @param config            Server configuration.
     */
    public NodeRunner(int nodeId, int nodeCount, Config config) {
        try {
            iface = new DA_Peterson(nodeId, nodeCount, config);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            Naming.bind(iface.getNodeUrl(), (DA_Peterson_RMI) iface);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * The run method starts the local interface of our distributed algorithm.
     */
    @Override
    public void run() {
        iface.start();
    }
}