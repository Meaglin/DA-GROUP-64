package com.meaglin.assignment3;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class Slave extends Server implements Slave_RMI {


    public static Node[] nodes;
    public static Node[] myNodes;

    public Slave() throws RemoteException {

    }

    public static void main(String[] args) throws Exception {
        String myIp = determineIP();
        if (myIp == null) {
            System.out.println("Cannot determine ip, exiting");
            return;
        }
        System.out.println("My IP: " + myIp);
        setupServer();

        Slave slave = new Slave();
        Naming.bind("rmi://127.0.0.1/slave", slave);

        Master_RMI master = (Master_RMI) Naming.lookup("rmi://145.0.0.0/master");
        myNodes = master.register(5, myIp);
    }

    @Override
    public void start(Node[] nodes) {
        Slave.nodes = nodes;
    }
}
