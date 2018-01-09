package example.music.entity;

import android.os.Parcel;
import android.os.Parcelable;


public class MediaBean implements Parcelable {
    public String path;
    public String name;
    public long time;
    public int mediaType;
    public long size;
    public int id;
    public String parentDir;

    public MediaBean() {
    }

    public MediaBean(String path, String name, long time, int mediaType, long size, int id, String parentDir) {
        this.path = path;
        this.name = name;
        this.time = time;
        this.mediaType = mediaType;
        this.size = size;
        this.id = id;
        this.parentDir = parentDir;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeLong(this.time);
        dest.writeInt(this.mediaType);
        dest.writeDouble(this.size);
        dest.writeInt(this.id);
        dest.writeString(this.parentDir);
    }

    protected MediaBean(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
        this.time = in.readLong();
        this.mediaType = in.readInt();
        this.size = in.readLong();
        this.id = in.readInt();
        this.parentDir = in.readString();
    }

    public static final Creator<MediaBean> CREATOR = new Creator<MediaBean>() {
        @Override
        public MediaBean createFromParcel(Parcel source) {
            return new MediaBean(source);
        }

        @Override
        public MediaBean[] newArray(int size) {
            return new MediaBean[size];
        }
    };


    @Override
    public String toString() {
        return "Media{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", mediaType=" + mediaType +
                ", size=" + size +
                ", id=" + id +
                ", parentDir='" + parentDir + '\'' +
                '}';
    }
}
