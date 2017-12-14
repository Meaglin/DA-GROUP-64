package com.meaglin.assignment3.test;

import com.meaglin.assignment3.DA_Randomized_Bryzantine_Agreement;

public class BaseTest {
    void reportStart(DA_Randomized_Bryzantine_Agreement[] ifaces) {
        String values = "";
        for(DA_Randomized_Bryzantine_Agreement iface : ifaces) {
            if (!values.equals("")) {
                values += ", ";
            }
            values += iface.value;

        }
        System.out.println("Starting with " + ifaces.length + " nodes; Values: [" + values + "]");
    }

    void reportEnd(DA_Randomized_Bryzantine_Agreement[] ifaces) {
        String values = "";
        for(DA_Randomized_Bryzantine_Agreement iface : ifaces) {
            if (!values.equals("")) {
                values += ", ";
            }
            values += iface.value;

        }
        System.out.println("Ending after " + ifaces[0].round.id + " rounds; Values: [" + values + "]");
    }
}
