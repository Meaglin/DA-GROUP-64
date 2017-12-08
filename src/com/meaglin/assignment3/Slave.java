package com.meaglin.assignment3;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Slave extends Server implements Slave_RMI {


    public static Node[] nodes;
    public static Node[] myNodes;

    List<DA_Randomized_Bryzantine_Agreement> das = new ArrayList<>();

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
        Naming.bind("rmi://127.0.0.1:1099/slave", (Slave_RMI) slave);

        Master_RMI master = (Master_RMI) Naming.lookup("rmi://145.94.229.78:1099/master");
        myNodes = master.register(5, myIp);
    }

    @Override
    public void notify(Node[] nodes) {
        CommunicationBus bus = new RMICommunicationBus();
        Slave.nodes = nodes;
        for (Node node : myNodes) {
            try {
                DA_Randomized_Bryzantine_Agreement da = new DA_Randomized_Bryzantine_Agreement(node.id, nodes, bus);
                das.add(da);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        for(DA_Randomized_Bryzantine_Agreement da : das) {
            new Thread(da).start();
        }
    }
}
