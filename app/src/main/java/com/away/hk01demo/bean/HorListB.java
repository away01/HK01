package com.away.hk01demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class HorListB implements Parcelable {
    public BaseFeedB feed;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.feed, flags);
    }

    public HorListB() {
    }

    protected HorListB(Parcel in) {
        this.feed = in.readParcelable(BaseFeedB.class.getClassLoader());
    }

    public static final Parcelable.Creator<HorListB> CREATOR = new Parcelable.Creator<HorListB>() {
        @Override
        public HorListB createFromParcel(Parcel source) {
            return new HorListB(source);
        }

        @Override
        public HorListB[] newArray(int size) {
            return new HorListB[size];
        }
    };
}
