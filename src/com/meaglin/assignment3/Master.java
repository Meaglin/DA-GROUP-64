package com.meaglin.assignment3;


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Master extends Server implements Master_RMI {

    static List<Node> nodes = new ArrayList<>();
    static List<String> slaves = new ArrayList<>();
    static Node[] myNodes;

    public Master() throws RemoteException {
    }

    public static void main(String[] args) throws Exception {
        String myIp = determineIP();
        if (myIp == null) {
            System.out.println("Cannot determine ip, exiting");
            return;
        }
        System.out.println("My IP: " + myIp);
        setupServer();

        Master master = new Master();
        Naming.bind("rmi://127.0.0.1/master", master);

        while(true) {
            Thread.sleep(1000);
            synchronized (nodes) {
                if (nodes.size() > 0) {
                    break;
                }
            }
        }
        myNodes = master.register(5, myIp);
        for(String ip : slaves) {
            Slave_RMI slave = (Slave_RMI) Naming.lookup("rmi://" + ip + "/slave");
            slave.start(nodes.toArray(new Node[nodes.size()]));
        }
    }


    @Override
    public Node[] register(int nodeCount, String ip) throws RemoteException {
        slaves.add(ip);
        Node[] newNodes = new Node[nodeCount];
        synchronized (nodes) {
            for (int i = 0; i < nodeCount; i++) {
                newNodes[i] = new Node(nodes.size(), ip);
                nodes.add(newNodes[i]);
            }
        }
        return newNodes;
    }
}
