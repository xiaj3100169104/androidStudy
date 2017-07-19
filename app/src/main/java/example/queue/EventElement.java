package example.queue;

/**
 * Created by xiajun on 2017/7/19.
 */

public class EventElement {
    //订阅者哈希编码
    public int subscriberHashCode;
    //事件码，唯一标识
    public int code;
    //数据
    public Object data;

    public EventElement(int subscriberHashCode, int code, Object data) {
        this.subscriberHashCode = subscriberHashCode;
        this.code = code;
        this.data = data;
    }
}