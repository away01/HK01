package com.away.hk01demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class LabelB implements Parcelable {

    /**
     * label : http://itunes.apple.com/favicon.ico
     */

    public String label;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
    }

    public LabelB() {
    }

    protected LabelB(Parcel in) {
        this.label = in.readString();
    }

    public static final Parcelable.Creator<LabelB> CREATOR = new Parcelable.Creator<LabelB>() {
        @Override
        public LabelB createFromParcel(Parcel source) {
            return new LabelB(source);
        }

        @Override
        public LabelB[] newArray(int size) {
            return new LabelB[size];
        }
    };
}
