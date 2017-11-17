package com.meaglin;

import java.util.Arrays;

public class PendingMessage {
    public final int node;
    public final String message;
    public final int[] clock;

    PendingMessage(int node, String message, int[] clock) {
        this.node = node;
        this.message = message;
        this.clock = clock;
    }

    public boolean canAccept(int[] localClock) {
        int[] V = localClock.clone();
        V[node] += 1;
        for(int i = 0; i < V.length; i += 1) {
            if (V[i] < clock[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object other) {
        if (other == null || !(other instanceof  PendingMessage)) {
            return false;
        }
        return Arrays.equals(((PendingMessage) other).clock, clock) && ((PendingMessage)other).node == node && ((PendingMessage)other).message.equals(message);
    }

    public String toString() {
        return "PendMessage(node=" + node + ",clock={" + Arrays.toString(clock) + "},message=" + message + ")";
    }
}