package com.meaglin.assignment3;

import java.rmi.RemoteException;

public class DA_Randomized_Bryzantine_Agreement_Faulty_Flip extends DA_Randomized_Bryzantine_Agreement {
    public DA_Randomized_Bryzantine_Agreement_Faulty_Flip(int nodeId, Node[] nodes, CommunicationBus bus) throws RemoteException {
        super(nodeId, nodes, bus);
    }

    @Override
    public void broadcast(int value) {
        if ((value == 0 || value == 1) && Math.random() < 0.5) { // Randomly flip
            value = value == 1 ? 0 : 1;
        }
        super.broadcast(value);
    }
}
