package com.meaglin.assignment3;

import java.rmi.Naming;

public class RMICommunicationBus implements CommunicationBus {

    @Override
    public void register(Node node, DA_Randomized_Bryzantine_Agreement da) {
        try {
            Naming.bind("rmi://127.0.0.1:1099/node_" + node.id, da);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Node node, Message msg) {
        try {
            DA_Randomized_Bryzantine_Agreement_RMI iface = (DA_Randomized_Bryzantine_Agreement_RMI) Naming.lookup(node.getUrl());
            iface.receive(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
