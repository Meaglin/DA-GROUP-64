package com.meaglin.assignment3;

public interface CommunicationBus {

    void register(Node node, DA_Randomized_Bryzantine_Agreement da);
    void send(Node node, Message msg);
}
