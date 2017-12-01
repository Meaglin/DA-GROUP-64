package com.meaglin.assignment3;

import java.rmi.Remote;

public interface Slave_RMI extends Remote {


    void notify(Node[] nodes);

    void start();
}
