package com.away.hk01demo.bean.lookup;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LookupB implements Parcelable {
    public int resultCount;
    public ArrayList<ResultsBean> results;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resultCount);
        dest.writeTypedList(this.results);
    }

    public LookupB() {
    }

    protected LookupB(Parcel in) {
        this.resultCount = in.readInt();
        this.results = in.createTypedArrayList(ResultsBean.CREATOR);
    }

    public static final Parcelable.Creator<LookupB> CREATOR = new Parcelable.Creator<LookupB>() {
        @Override
        public LookupB createFromParcel(Parcel source) {
            return new LookupB(source);
        }

        @Override
        public LookupB[] newArray(int size) {
            return new LookupB[size];
        }
    };
}
