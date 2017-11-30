package com.meaglin.assignment3;

import java.io.Serializable;

public class Message implements Serializable {

    // The nodeId
    final int nodeId;
    // Message type 1 = notification, 2 = proposal
    final int type;
    // Round id
    final int round;
    // Value 0 or 1
    final int value;

    public Message(int nodeId, int type, int round, int value) {
        this.nodeId = nodeId;
        this.type = type;
        this.round = round;
        this.value = value;
    }
}
