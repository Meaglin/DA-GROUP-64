package com.meaglin.assignment3;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DA_Randomized_Bryzantine_Agreement extends UnicastRemoteObject implements Runnable, DA_Randomized_Bryzantine_Agreement_RMI {


    int id, nodeCount;

    Round round;

    List<Message> bufferedMessages;

    Node[] nodes;

    volatile int value;

    boolean decided;

    public DA_Randomized_Bryzantine_Agreement(int nodeId, Node[] nodes) throws RemoteException {
        super();
        id = nodeId;
        this.nodeCount = nodes.length;
        this.nodes = nodes;


        bufferedMessages = new ArrayList<>();

        try {
            Naming.bind("rmi://127.0.0.1:1099/node_" + id, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        start();
    }

    public void start() {
        synchronized (this) {
            round = new Round(0, nodeCount);
//            value = 1;
            value = (int) Math.floor(Math.random() * 2);
            decided = false;
            System.out.println("[Node " + id + "] starting round 0 with value " + value);
        }
        broadcast(value);
    }
    /*

    proc Decison
        begin
            if count^R_i(O) > count^R_i(l) then
                majority := 0
            else
                majority := 1;
            if count^R_i (majority) > C
            then message^R_i := majority
            else message^R_i:= secret^R_i;
        end
    proc PhaseR
        begin
            Exchange messageValues;
            Lottery;
            Decision
        end
    proc ByzantineAgreement
        begin
            R := O;
            do while (R # LastPhase)
                begin
                R := R + 1;
                PhaseR;
            end
        end

    n/5 > f
    r←1
    decided← false
        do forever
            # Notification phase
            broadcast(N;r,v)
            await n−f messages of the form (N;r,*)
            # Proposal phase
            if(>(n+f)/2 messages (N;r,w) received with w=0 or 1)then
                broadcast(P;r,w)
            else
                broadcast(P;r,?)
            if decided then
                STOP
            else
                await n−f messages of the form (P,r,*)
            # Decision phase
            if (> f messages (P;r,w) received with w=0 or 1) then
                v←w
                if (> 3f messages (P;r,w)) then
                    decide w
                    decided ← true
            else
                v ← random(0,1)
            r←r+1

     */
    @Override
    public void receive(Message message) throws RemoteException {
        System.out.println("[Node " + id + "] receiving " + message.value);
        int nextBroadcast;
        try {
            Thread.sleep((long) (Math.random() * 5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        nextBroadcast = processMessage(message);
        if (nextBroadcast != -1) {
            broadcast(nextBroadcast);
        }
        checkBuffer();
    }

    private int processMessage(Message message) {
        synchronized(this) {
            if (decided) {
                return -1;
            }
            if (!isValidMessage(message)) {
                return -1;
            }
            // Discard messages of uninteresting rounds (previous rounds)
            if (message.round < round.id) {
                return -1;
            }
            if (message.round > round.id) {
                bufferedMessages.add(message);
                return -1;
            }
            System.out.println("[Node " + id + "] processing");
            round.processMessage(message);
            if (round.phase == Round.Phase.NOTIFICATION &&
                    round.notificationThresholdReached()) {
                round.phase = Round.Phase.PROPOSAL;
                return round.notificationConsensus();
            } else if (round.phase == Round.Phase.PROPOSAL &&
                    round.proposalThresholdReached()) {

                round.phase = Round.Phase.DECISION;
                int lowConsensus = round.proposalLowConsensus();
                if (lowConsensus == 0 || lowConsensus == 1) {
                    value = lowConsensus;
                    int decidedConsensus = round.proposalDecidedConsensus();
                    if (decidedConsensus == 0 || decidedConsensus == 1) {
                        decided = true;
                        value = decidedConsensus;
                        System.out.println("[Node " + id + "] decided " + value);
                        return -1;
                    }
                } else {
                    value = (int) Math.floor(Math.random() * 2);
                }
                round = new Round(round.id + 1, nodeCount);

                System.out.println("[Node " + id + "] starting round " + round.id);

                return value;
            } else {
                return -1;
            }
        }
    }

    public void checkBuffer() throws RemoteException {
        Message msg = null;
        synchronized (this) {
            Iterator<Message> it = bufferedMessages.iterator();
            while(it.hasNext()) {
                Message next = it.next();
                if (next.round <= round.id) {
                    it.remove();
                    msg = next;
                    break;
                }
            }
        }
        if (msg != null) {
            receive(msg);
        }
    }

    public void broadcast(int value) {
        Message message = null;
        synchronized(this) {
            if (!round.hasNotified) {
                message = new Message(id, 1, round.id, value);
                round.hasNotified = true;
            } else if (!round.hasProposed) {
                message = new Message(id, 2, round.id, value);
                round.hasProposed = true;
            }
        }
        if (message == null) {
            return;
        }
        System.out.println("[Node " + id + "] broadcasting " + message.value);
        for(int i = 0; i < nodeCount; i++) {
            Node node = nodes[i];
            node.sendMessage(message);
        }
    }

    public boolean isValidMessage(Message message) {
        return (message.round >= 0 &&
                message.nodeId >= 0 &&
                message.nodeId < nodeCount &&
                (message.value == 0 || message.value == 1 || message.value == -2) &&
                (message.type == 1 || message.type == 2)
        );
    }
}
