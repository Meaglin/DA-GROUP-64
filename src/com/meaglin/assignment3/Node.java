package com.meaglin.assignment3;

import java.io.Serializable;
import java.rmi.Naming;

public class Node implements Serializable {
    final int id;
    final String ip;

    public Node(int id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public String getUrl() {
        return "rmi://" + ip + ":1099/node_" + id;
    }
}
