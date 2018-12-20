package example.queue;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by xiajun on 2017/7/5.
 * 自定义事件订阅管理类，
 */

public class EventManager {
    private static final String TAG = "EventManager";
    private static EventManager instance;
    /**
     * 如果有1 2 3这3个Entry，那么访问了1，就把1移到尾部去，即2 3 1。
     * 每次访问都把访问的那个数据移到双向队列的尾部去，那么每次要淘汰数据的时候，双向队列最头的那个数据不就是最不常访问的那个数据了吗？
     * 换句话说，双向链表最头的那个数据就是要淘汰的数据。
     * 其核心思想是“如果数据最近被访问过，那么将来被访问的几率也更高”。
     * <p>
     * 有链表结构：查询慢，插入删除快。
     * 无链表结构：查询快，插入删除慢。
     * 这里需要查询快，所以使用无链表结构集合。
     */
    private HashMap<Object, HashSet<Integer>> subscriberMap = new HashMap<>();

    /**
     * ui handler
     */
    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    public static EventManager getInstance() {
        synchronized (EventManager.class) {
            if (instance == null)
                instance = new EventManager();
        }
        return instance;
    }

    /**
     * 订阅事件
     *
     * @param subscriber
     * @param code
     */
    public synchronized void register(Object subscriber, int code) {
        if (subscriber == null)
            return;
        if (!subscriberMap.containsKey(subscriber)) {
            HashSet<Integer> codes = new HashSet<>();
            codes.add(code);
            subscriberMap.put(subscriber, codes);
        } else
            subscriberMap.get(subscriber).add(code);
    }

    /**
     * 取消订阅
     *
     * @param subscriber
     */
    public synchronized void unRegister(Object subscriber) {
        if (subscriberMap.containsKey(subscriber)) {
            subscriberMap.remove(subscriber);
        }
    }

    /**
     * 接收事件
     *
     * @param code
     * @param data
     */
    public void post(final int code, final Object data) {
        ThreadPoolUtil.execute(new ReceiveEventTask(code, data, subscriberMap, mUIHandler));
    }
}
