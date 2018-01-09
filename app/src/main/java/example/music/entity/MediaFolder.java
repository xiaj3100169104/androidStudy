package example.music.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MediaFolder implements Parcelable {

    public String name;

    public int count;

    ArrayList<MediaBean> medias = new ArrayList<>();

    public void addMedias(MediaBean media) {
        medias.add(media);
    }

    public MediaFolder(String name) {
        this.name = name;
    }

    public ArrayList<MediaBean> getMedias() {
        return this.medias;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.count);
        dest.writeTypedList(this.medias);
    }


    protected MediaFolder(Parcel in) {
        this.name = in.readString();
        this.count = in.readInt();
        this.medias = in.createTypedArrayList(MediaBean.CREATOR);
    }

    public static final Creator<MediaFolder> CREATOR = new Creator<MediaFolder>() {
        @Override
        public MediaFolder createFromParcel(Parcel source) {
            return new MediaFolder(source);
        }

        @Override
        public MediaFolder[] newArray(int size) {
            return new MediaFolder[size];
        }
    };
}
