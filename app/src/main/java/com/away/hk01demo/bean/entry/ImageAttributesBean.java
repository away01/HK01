package com.away.hk01demo.bean.entry;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageAttributesBean implements Parcelable {

    /**
     * height : 53
     */

    public String height;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.height);
    }

    public ImageAttributesBean() {
    }

    protected ImageAttributesBean(Parcel in) {
        this.height = in.readString();
    }

    public static final Parcelable.Creator<ImageAttributesBean> CREATOR = new Parcelable.Creator<ImageAttributesBean>() {
        @Override
        public ImageAttributesBean createFromParcel(Parcel source) {
            return new ImageAttributesBean(source);
        }

        @Override
        public ImageAttributesBean[] newArray(int size) {
            return new ImageAttributesBean[size];
        }
    };
}