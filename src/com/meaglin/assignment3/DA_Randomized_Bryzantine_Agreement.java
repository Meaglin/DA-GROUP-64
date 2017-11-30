package com.meaglin.assignment3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class DA_Randomized_Bryzantine_Agreement extends UnicastRemoteObject implements DA_Randomized_Bryzantine_Agreement_RMI {


    int id, nodeCount;

    Round round;

    List<Message> bufferedMessages;

    int value;

    boolean decided;

    public DA_Randomized_Bryzantine_Agreement(int nodeId, int nodeCount) throws RemoteException {
        super();
        id = nodeId;
        this.nodeCount = nodeCount;
    }

    public synchronized void start() {
        round = new Round(0, nodeCount);
        value = (int) (Math.random() * 2);
        decided = false;
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
    public synchronized void receive(Message message) throws RemoteException {
        if (decided) {
            return;
        }
        try {
            Thread.sleep((long) (Math.random() * 50));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!isValidMessage(message)) {
            return;
        }
        // Discard messages of uninteresting rounds (previous rounds)
        if (message.round < round.id) {
            return;
        }
        if (message.round > round.id) {
            bufferedMessages.add(message);
        }
        round.processMessage(message);
        if (round.phase == Round.Phase.NOTIFICATION &&
                round.notificationThresholdReached()) {
            round.phase = Round.Phase.PROPOSAL;
            broadcast(round.notificationConsensus());
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
                    return;
                }
            }
            round = new Round(round.id + 1, nodeCount);
            broadcast(value);
        }
    }

    public void broadcast(int value) {
        Message message;
        if (!round.hasNotified) {
            message = new Message(id, 1, round.id, value);
            round.hasNotified = true;
            round.processMessage(message);
        } else if (!round.hasProposed) {
            message = new Message(id, 2, round.id, value);
            round.hasProposed = true;
            round.processMessage(message);
        }
        // TODO: broadcast
    }

    public boolean isValidMessage(Message message) {
        return (message.round >= 0 &&
                message.nodeId >= 0 &&
                message.nodeId < nodeCount &&
                (message.value == 0 || message.value == 1) &&
                (message.type == 1 || message.type == 2)
        );
    }
}
