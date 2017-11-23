package com.meaglin.assignment1;

import java.rmi.Remote;

public interface DA_Birman_Schiper_Stephen_RMI extends Remote {

    public void receive(int id, String message, int[] clock) throws java.rmi.RemoteException;
}
