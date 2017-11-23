package com.meaglin.assignment2;

import java.rmi.Remote;

public interface DA_Peterson_RMI extends Remote {


    public void receive(int id) throws java.rmi.RemoteException;
}
