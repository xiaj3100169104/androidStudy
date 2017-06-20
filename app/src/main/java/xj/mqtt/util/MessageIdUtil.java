package xj.mqtt.util;

import java.util.concurrent.atomic.AtomicLong;

import xj.mqtt.util.StringUtil;

/**
 * Created by xiajun on 2017/6/20.
 */

public class MessageIdUtil {
    /**
     * A prefix helps to make sure that ID's are unique across multiple instances.
     */
    private static final String PREFIX = StringUtil.randomString(5) + "-";

    /**
     * Keeps track of the current increment, which is appended to the prefix to
     * forum a unique ID.
     */
    private static final AtomicLong ID = new AtomicLong();

    public static String newMessageId() {
        return PREFIX + Long.toString(ID.incrementAndGet());
    }
}
