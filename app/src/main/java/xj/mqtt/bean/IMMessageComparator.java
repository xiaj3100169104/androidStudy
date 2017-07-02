package xj.mqtt.bean;


import java.util.Comparator;

public class IMMessageComparator implements Comparator<IMMessage> {
    public int compare(IMMessage o1, IMMessage o2) {

        return String.valueOf(o1.createTime).compareTo(String.valueOf(o2.createTime));

    }
}
