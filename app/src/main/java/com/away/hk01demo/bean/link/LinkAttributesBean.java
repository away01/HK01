package com.away.hk01demo.bean.link;

import android.os.Parcel;
import android.os.Parcelable;

public class LinkAttributesBean implements Parcelable {
    public String rel;
    public String href;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rel);
        dest.writeString(this.href);
    }

    public LinkAttributesBean() {
    }

    protected LinkAttributesBean(Parcel in) {
        this.rel = in.readString();
        this.href = in.readString();
    }

    public static final Parcelable.Creator<LinkAttributesBean> CREATOR = new Parcelable.Creator<LinkAttributesBean>() {
        @Override
        public LinkAttributesBean createFromParcel(Parcel source) {
            return new LinkAttributesBean(source);
        }

        @Override
        public LinkAttributesBean[] newArray(int size) {
            return new LinkAttributesBean[size];
        }
    };
}
