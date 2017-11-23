package com.meaglin.assignment1;

import java.rmi.Naming;

public class ClientRunner implements Runnable {

    private DA_Birman_Schiper_Stephen iface;
    private int id, nodeCount;
    public ClientRunner(int id, int nodeCount) {
        this.id = id;
        this.nodeCount = nodeCount;
        try {
            iface = new DA_Birman_Schiper_Stephen(id, nodeCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
//            Naming.bind("rmi://127.0.0.1:1099/p" + id, iface);

            Thread.sleep(100);

//            Thread.sleep(getRandom(1000));
            iface.broadcast("Hallo iedereen");
            iface.broadcast("Hallo iedereen2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getRandom(long ceil) {
        return (long) (Math.random() * ceil);
    }
}
