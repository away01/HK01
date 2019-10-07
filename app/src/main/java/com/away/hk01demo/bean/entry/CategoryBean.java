package com.away.hk01demo.bean.entry;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryBean implements Parcelable {

    /**
     * attributes : {"im:id":"6014","term":"Games","scheme":"https://apps.apple.com/hk/genre/ios-%E9%81%8A%E6%88%B2/id6014?uo=2","label":"遊戲"}
     */

    public CategoryAttributesBean attributes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.attributes, flags);
    }

    public CategoryBean() {
    }

    protected CategoryBean(Parcel in) {
        this.attributes = in.readParcelable(CategoryAttributesBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<CategoryBean> CREATOR = new Parcelable.Creator<CategoryBean>() {
        @Override
        public CategoryBean createFromParcel(Parcel source) {
            return new CategoryBean(source);
        }

        @Override
        public CategoryBean[] newArray(int size) {
            return new CategoryBean[size];
        }
    };
}