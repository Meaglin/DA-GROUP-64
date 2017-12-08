package com.meaglin.assignment2;

import java.rmi.Remote;

/**
 * Remote interface of Peterson's election algorithm in a unidirectional ring.
 */
public interface DA_Peterson_RMI extends Remote {

    public void receive(int id) throws java.rmi.RemoteException;

}