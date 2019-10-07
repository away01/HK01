package com.away.hk01demo.bean.entry;

import android.os.Parcel;
import android.os.Parcelable;

public class Im_ImageB implements Parcelable {

    /**
     * label : https://is5-ssl.mzstatic.com/image/thumb/Purple113/v4/5c/ca/38/5cca3841-eb77-c39c-91bd-81bf99713e25/AppIcon-0-1x_U007emarketing-0-85-220-0-7.png/53x53bb.png
     * attributes : {"height":"53"}
     */

    public String label;
    public ImageAttributesBean attributes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeParcelable(this.attributes, flags);
    }

    public Im_ImageB() {
    }

    protected Im_ImageB(Parcel in) {
        this.label = in.readString();
        this.attributes = in.readParcelable(ImageAttributesBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Im_ImageB> CREATOR = new Parcelable.Creator<Im_ImageB>() {
        @Override
        public Im_ImageB createFromParcel(Parcel source) {
            return new Im_ImageB(source);
        }

        @Override
        public Im_ImageB[] newArray(int size) {
            return new Im_ImageB[size];
        }
    };
}
