package com.meaglin.assignment3;

public class Round {

    enum Phase {
        NOTIFICATION,
        PROPOSAL,
        DECISION
    }

    public final int id, nodeCount, failureCount;

    Phase phase = Phase.NOTIFICATION;

    boolean hasNotified = false, hasProposed = false;

    /*
        values:
            0 or 1: found value
            -1: no value yet received
            -2: node cannot find consensus(only for proposals)
     */
    int[] receivedNotifications;
    int[] receivedProposals;

    int receivedNotificationCount;
    int receivedProposalCount;

    Round(int roundId, int nodeCount) {
        id = roundId;
        this.nodeCount = nodeCount;

        this.failureCount = (int) Math.floor(((double) nodeCount) / 5.0);

        receivedNotifications = new int[nodeCount];
        receivedProposals = new int[nodeCount];
        for(int i = 0; i < nodeCount; i++) {
            receivedNotifications[i] = -1;
            receivedProposals[i] = -1;
        }
    }

    void processMessage(Message message) {
        if (message.type == 1) {
            // Don't allow overriding
            if (receivedNotifications[message.nodeId] == -1) {
                receivedNotifications[message.nodeId] = message.value;
                receivedNotificationCount++;
            }
        } else if(message.type == 2) {
            // Don't allow overriding
            if (receivedProposals[message.nodeId] == -1) {
                receivedProposals[message.nodeId] = message.value;
                receivedProposalCount++;
            }
        }
    }

    /**
     * Determine the notification consensus value
     *
     * @return 0 or 1 if there is more then threshold of those values
     *      -2 if no consensus could be reached
     */
    int notificationConsensus() {
        int threshold;
        if ( (nodeCount+failureCount)% 2 != 0) {
            threshold = (nodeCount+failureCount-1) / 2;
        } else {
            threshold = (nodeCount+failureCount) / 2;
        }
        int cnt0 = 0, cnt1 = 0;
        for(int i = 0; i < nodeCount;i++) {
            if (receivedNotifications[i] == 0) {
                cnt0++;
            } else if (receivedNotifications[i] == 1) {
                cnt1++;
            }
        }
        if (cnt0 >= cnt1 && cnt0 > threshold) {
            return 0;
        }
        if (cnt1 >= cnt0 && cnt1 > threshold) {
            return 1;
        }
        return -2;
    }

    int proposalLowConsensus() {
        int threshold = failureCount;
        int cnt0 = 0, cnt1 = 0;
        for(int i = 0; i < nodeCount;i++) {
            if (receivedProposals[i] == 0) {
                cnt0++;
            } else if (receivedProposals[i] == 1) {
                cnt1++;
            }
        }
        if (cnt0 >= cnt1 && cnt0 > threshold) {
            return 0;
        }
        if (cnt1 >= cnt0 && cnt1 > threshold) {
            return 1;
        }
        return -2;
    }

    int proposalDecidedConsensus() {
        int threshold = failureCount * 3;
        int cnt0 = 0, cnt1 = 0;
        for(int i = 0; i < nodeCount;i++) {
            if (receivedProposals[i] == 0) {
                cnt0++;
            } else if (receivedProposals[i] == 1) {
                cnt1++;
            }
        }
        if (cnt0 >= cnt1 && cnt0 > threshold) {
            return 0;
        }
        if (cnt1 >= cnt0 && cnt1 > threshold) {
            return 1;
        }
        return -2;
    }

    boolean notificationThresholdReached() {
        return (receivedNotificationCount >= (nodeCount - failureCount));
    }

    boolean proposalThresholdReached() {
        return (receivedProposalCount >= (nodeCount - failureCount));
    }
}
