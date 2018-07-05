package example.address;

/**
 * Created by xiajun on 2018/7/5.
 */

public class MyRingtone {
    public final String id;
    public final String name;
    public final String path;
    public final String is_ringtone;
    public final String is_notification;

    public MyRingtone(String id, String name, String path, String is_ringtone, String is_notification) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.is_ringtone = is_ringtone;
        this.is_notification = is_notification;
    }

    @Override
    public String toString() {
        return "MyRingtone{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", is_ringtone='" + is_ringtone + '\'' +
                ", is_notification='" + is_notification + '\'' +
                '}';
    }
}
