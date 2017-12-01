package com.meaglin.assignment3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Master_RMI extends Remote {



    Node[] register(int nodeCount, String ip) throws RemoteException;
}
