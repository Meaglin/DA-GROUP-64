package com.meaglin.assignment3;

import java.rmi.RemoteException;

public interface Master_RMI {



    Node[] register(int nodeCount, String ip) throws RemoteException;
}
