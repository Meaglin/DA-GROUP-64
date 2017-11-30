package com.meaglin.assignment3;

import java.io.Serializable;

public class Node implements Serializable {
    final int id;
    final String ip;

    public Node(int id, String ip) {
        this.id = id;
        this.ip = ip;
    }
}
