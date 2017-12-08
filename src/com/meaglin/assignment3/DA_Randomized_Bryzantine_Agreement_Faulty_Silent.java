package com.meaglin.assignment3;

import java.rmi.RemoteException;

public class DA_Randomized_Bryzantine_Agreement_Faulty_Silent extends DA_Randomized_Bryzantine_Agreement {
    public DA_Randomized_Bryzantine_Agreement_Faulty_Silent(int nodeId, Node[] nodes, CommunicationBus bus) throws RemoteException {
        super(nodeId, nodes, bus);
    }

    @Override
    public void broadcast(int value) {
        return; // Do nothing >:)
    }
}
