package com.away.hk01demo.bean.entry;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryAttributesBean implements Parcelable {
    public String im_id;
    public String term;
    public String scheme;
    public String label;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.im_id);
        dest.writeString(this.term);
        dest.writeString(this.scheme);
        dest.writeString(this.label);
    }

    public CategoryAttributesBean() {
    }

    protected CategoryAttributesBean(Parcel in) {
        this.im_id = in.readString();
        this.term = in.readString();
        this.scheme = in.readString();
        this.label = in.readString();
    }

    public static final Parcelable.Creator<CategoryAttributesBean> CREATOR = new Parcelable.Creator<CategoryAttributesBean>() {
        @Override
        public CategoryAttributesBean createFromParcel(Parcel source) {
            return new CategoryAttributesBean(source);
        }

        @Override
        public CategoryAttributesBean[] newArray(int size) {
            return new CategoryAttributesBean[size];
        }
    };
}