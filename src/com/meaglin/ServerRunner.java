package com.meaglin;

import java.rmi.Naming;

public class ServerRunner implements Runnable {


    public static int nodeCount = 3;
    public static DA_Birman_Schiper_Stephen[] nodes;
    public void run() {
        try {
            nodes = new DA_Birman_Schiper_Stephen[nodeCount];
            for (int i = 0; i < nodeCount; i += 1) {
                nodes[i] = new DA_Birman_Schiper_Stephen(i, nodeCount);
                Naming.bind("p" + i, nodes[i]);
            }
            nodes[0].broadcast("hello world1");
            nodes[0].broadcast("hello world2");
            nodes[1].broadcast("hello world3");
            nodes[1].broadcast("hello world4");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
