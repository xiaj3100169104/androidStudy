package example.queue;

/**
 * Created by xiajun on 2017/7/19.
 */

public interface EventReceiver {
    void onMainThreadEvent(int code, Object data);
}
