package com.meaglin.assignment3;

import java.rmi.RemoteException;

public class LocalCommunicationBus implements CommunicationBus {

    int nodeCount;
    DA_Randomized_Bryzantine_Agreement[] interfaces;
    Node[] nodes;

    public LocalCommunicationBus(int nodeCount) {
        this.nodeCount = nodeCount;
        interfaces = new DA_Randomized_Bryzantine_Agreement[nodeCount];
        nodes = new Node[nodeCount];
    }

    @Override
    public void register(Node node, DA_Randomized_Bryzantine_Agreement da) {
        nodes[node.id] = node;
        interfaces[node.id] = da;
    }

    @Override
    public void send(Node node, Message msg) {
        try {
            interfaces[node.id].receive(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
