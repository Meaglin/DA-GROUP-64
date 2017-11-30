package com.meaglin.assignment3;

import java.rmi.Remote;

public interface DA_Randomized_Bryzantine_Agreement_RMI extends Remote {

    public void receive(Message message) throws java.rmi.RemoteException;
}
