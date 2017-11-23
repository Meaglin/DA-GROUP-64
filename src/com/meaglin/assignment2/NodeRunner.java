package com.meaglin.assignment2;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class NodeRunner implements Runnable {


    DA_Peterson iface;
    public NodeRunner(int nodeId, int nodeCount, Config config) throws RemoteException {
        iface = new DA_Peterson(nodeId, nodeCount, config);
        try {
            Naming.bind(iface.getNodeUrl(), (DA_Peterson_RMI) iface);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        iface.start();
    }
}
