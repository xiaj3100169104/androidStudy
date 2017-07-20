package example.queue;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiajun on 2017/7/5.
 */

public class EventManager {
    private static final String TAG = "EventManager";
    private static EventManager instance;
    private HashMap<Object, LinkedHashSet<Integer>> subscriberMap = new HashMap<>();

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

    public void register(Object subscriber, int code) {
        if (subscriber == null)
            return;
        if (subscriberMap.containsKey(subscriber)){
            LinkedHashSet<Integer> list = subscriberMap.get(subscriber);

        }
        if (!subscriberMap.containsKey(subscriber))
            subscriberMap.put(subscriber, new LinkedHashSet<Integer>());
        subscriberMap.get(subscriber).add(code);
    }

    public void unRegister(Object subscriber){
        if (subscriberMap.containsKey(subscriber)){
            subscriberMap.remove(subscriber);
        }
    }

    public void post(int code, Object data) {
        for (Object subscriber : subscriberMap.keySet()) {
            LinkedHashSet<Integer> list = subscriberMap.get(subscriber);
            for (Integer eventCode : list) {
                if (eventCode.hashCode() == code) {
                    EventElement event = new EventElement(subscriber.hashCode(), eventCode, data);
                    dispatchEvent(event);
                    break;
                }
            }
        }
    }

    private void dispatchEvent(EventElement event) {
        for (Object subscriber : subscriberMap.keySet()) {
            if (subscriber.hashCode() == event.subscriberHashCode) {
                handleEventOnMainThread(subscriber, event);
                break;
            }
        }
    }

    private void handleEventOnMainThread(final Object subscriber, final EventElement event) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Class<?> clazz = subscriber.getClass();
                    Method method = clazz.getMethod("onMainThreadEvent", int.class, Object.class);
                    method.invoke(subscriber, event.code, event.data);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
