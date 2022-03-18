package com.vkc.strabo.fcmservice;



import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by RIJO K JOSE
 */

public class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}