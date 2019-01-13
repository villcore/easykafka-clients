package com.villcore.easykafka.clients;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class BackOff {

    public abstract void execute();

    public static void main(String[] args) throws Exception {
        System.out.println("test");

        // initial interval.
        // multiplitr.
        // max interval.

        long initialIntervalMs = 1000L;
        long maxIntervalMs = 10 * 1000L;
        long intervalMs = initialIntervalMs;

        double multiplier = 1.5;
        long lastExecuteMs = 0L;

        while (true) {
            if (System.currentTimeMillis() - lastExecuteMs > intervalMs) {
                if (intervalMs < maxIntervalMs) {
                    intervalMs = (long) (intervalMs * multiplier);
                }
                // execute.

                lastExecuteMs = System.currentTimeMillis();
            }
        }
    }

}
