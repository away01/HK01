package com.away.hk01demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.away.hk01demo.bean.entry.EntryBean;

import java.util.ArrayList;
import java.util.List;

public class BaseFeedB implements Parcelable {
    public LabelB author;
    public LabelB updated;
    public LabelB rights;
    public LabelB title;
    public LabelB icon;
    public LabelB id;
    public ArrayList<EntryBean> entry;
    public List<LinkBeanData> link;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.author, flags);
        dest.writeParcelable(this.updated, flags);
        dest.writeParcelable(this.rights, flags);
        dest.writeParcelable(this.title, flags);
        dest.writeParcelable(this.icon, flags);
        dest.writeParcelable(this.id, flags);
        dest.writeTypedList(this.entry);
        dest.writeList(this.link);
    }

    public BaseFeedB() {
    }

    protected BaseFeedB(Parcel in) {
        this.author = in.readParcelable(LabelB.class.getClassLoader());
        this.updated = in.readParcelable(LabelB.class.getClassLoader());
        this.rights = in.readParcelable(LabelB.class.getClassLoader());
        this.title = in.readParcelable(LabelB.class.getClassLoader());
        this.icon = in.readParcelable(LabelB.class.getClassLoader());
        this.id = in.readParcelable(LabelB.class.getClassLoader());
        this.entry = in.createTypedArrayList(EntryBean.CREATOR);
        this.link = new ArrayList<>();
        in.readList(this.link, LinkBeanData.class.getClassLoader());
    }

    public static final Parcelable.Creator<BaseFeedB> CREATOR = new Parcelable.Creator<BaseFeedB>() {
        @Override
        public BaseFeedB createFromParcel(Parcel source) {
            return new BaseFeedB(source);
        }

        @Override
        public BaseFeedB[] newArray(int size) {
            return new BaseFeedB[size];
        }
    };
}
