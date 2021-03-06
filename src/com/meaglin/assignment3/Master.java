package com.meaglin.assignment3;


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Master extends Server implements Master_RMI {

    public static boolean DEBUG = false;

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
        System.out.println("test");
        CommunicationBus bus = new RMICommunicationBus();

        System.out.println("test");
        Naming.bind("rmi://127.0.0.1:1099/master", (Master_RMI) master);


        System.out.println("test");
        // Register my own nodes first
        int nodeCount = 7;
        myNodes = new Node[nodeCount];
        synchronized (nodes) {
            for (int i = 0; i < nodeCount; i++) {
                myNodes[i] = new Node(nodes.size(), myIp);
                nodes.add(myNodes[i]);
            }
        }

        while(true) {
            Thread.sleep(1000);
            synchronized (nodes) {
                if (nodes.size() >= nodeCount) {
                    break;
                }
            }
        }


        // Notify all nodes
        for(String ip : slaves) {
            System.out.println("rmi://" + ip + ":1099/slave");
            Slave_RMI slave = (Slave_RMI) Naming.lookup("rmi://" + ip + ":1099/slave");
            slave.notify(nodes.toArray(new Node[nodes.size()]));
        }



        // Initialize own nodes
        List<DA_Randomized_Bryzantine_Agreement> das = new ArrayList<>();
        for (Node node : myNodes) {
            try {
                DA_Randomized_Bryzantine_Agreement da = new DA_Randomized_Bryzantine_Agreement(node.id, nodes.toArray(new Node[nodes.size()]), bus);
                das.add(da);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        // Startup all nodes
        for (String ip: slaves) {
            Slave_RMI slave = (Slave_RMI) Naming.lookup("rmi://" + ip + ":1099/slave");
            slave.start();
        }
        for (DA_Randomized_Bryzantine_Agreement da : das) {
            new Thread(da).start();
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
