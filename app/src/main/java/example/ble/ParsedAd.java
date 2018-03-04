package example.ble;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by xiajun on 2018/3/3.
 */

public class ParsedAd {
    public byte flags;
    public ArrayList<UUID> uuids = new ArrayList<>();
    public String localName;
    public short manufacturer;

}
