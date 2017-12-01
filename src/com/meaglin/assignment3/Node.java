package com.meaglin.assignment3;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Node implements Serializable {
    final int id;
    final String ip;

    public Node(int id, String ip) {
        this.id = id;
        this.ip = ip;
    }


    public void sendMessage(Message message) {
        try {
            DA_Randomized_Bryzantine_Agreement_RMI iface = (DA_Randomized_Bryzantine_Agreement_RMI) Naming.lookup(getUrl());
            iface.receive(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return "rmi://" + ip + ":1099/node_" + id;
    }
}
