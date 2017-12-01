package com.meaglin.assignment3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Slave_RMI extends Remote {


    void notify(Node[] nodes) throws RemoteException;

    void start() throws RemoteException;
}
