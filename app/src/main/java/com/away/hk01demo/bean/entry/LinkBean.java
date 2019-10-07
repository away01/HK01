package com.away.hk01demo.bean.entry;

import android.os.Parcel;
import android.os.Parcelable;

import com.away.hk01demo.bean.link.LinkAttributesBean;

public class LinkBean implements Parcelable {

    /**
     * attributes : {"rel":"alternate","type":"text/html","href":"https://apps.apple.com/hk/app/captain-tsubasa-dream-team/id1293738123?uo=2"}
     */

    public LinkAttributesBean attributes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.attributes, flags);
    }

    public LinkBean() {
    }

    protected LinkBean(Parcel in) {
        this.attributes = in.readParcelable(LinkAttributesBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<LinkBean> CREATOR = new Parcelable.Creator<LinkBean>() {
        @Override
        public LinkBean createFromParcel(Parcel source) {
            return new LinkBean(source);
        }

        @Override
        public LinkBean[] newArray(int size) {
            return new LinkBean[size];
        }
    };
}
