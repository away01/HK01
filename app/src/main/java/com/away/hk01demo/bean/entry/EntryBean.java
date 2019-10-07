package com.away.hk01demo.bean.entry;

import android.os.Parcel;
import android.os.Parcelable;

import com.away.hk01demo.bean.LabelB;

import java.util.ArrayList;
import java.util.List;

public class EntryBean implements Parcelable {


    public LabelB im_name;
    public ArrayList<Im_ImageB> im_imageBList;
    public LabelB summary;
    public Im_PriceB im_priceB;
    public Im_ContentTypeB im_contentTypeB;
    public LabelB rights;
    public LabelB title;//
    public List<LinkBean> link;//
    public IdBean id;
    public Im_ArtistB im_artistB;
    public CategoryBean category;//
    public Im_ReleaseDateB im_releaseDateB;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.im_name, flags);
        dest.writeTypedList(this.im_imageBList);
        dest.writeParcelable(this.summary, flags);
        dest.writeParcelable((Parcelable) this.im_priceB, flags);
        dest.writeParcelable((Parcelable) this.im_contentTypeB, flags);
        dest.writeParcelable(this.rights, flags);
        dest.writeParcelable(this.title, flags);
        dest.writeList(this.link);
        dest.writeParcelable((Parcelable) this.id, flags);
        dest.writeParcelable((Parcelable) this.im_artistB, flags);
        dest.writeParcelable(this.category, flags);
        dest.writeParcelable((Parcelable) this.im_releaseDateB, flags);
    }

    public EntryBean() {
    }

    protected EntryBean(Parcel in) {
        this.im_name = in.readParcelable(LabelB.class.getClassLoader());
        this.im_imageBList = in.createTypedArrayList(Im_ImageB.CREATOR);
        this.summary = in.readParcelable(LabelB.class.getClassLoader());
        this.im_priceB = in.readParcelable(Im_PriceB.class.getClassLoader());
        this.im_contentTypeB = in.readParcelable(Im_ContentTypeB.class.getClassLoader());
        this.rights = in.readParcelable(LabelB.class.getClassLoader());
        this.title = in.readParcelable(LabelB.class.getClassLoader());
        this.link = new ArrayList<LinkBean>();
        in.readList(this.link, LinkBean.class.getClassLoader());
        this.id = in.readParcelable(IdBean.class.getClassLoader());
        this.im_artistB = in.readParcelable(Im_ArtistB.class.getClassLoader());
        this.category = in.readParcelable(CategoryBean.class.getClassLoader());
        this.im_releaseDateB = in.readParcelable(Im_ReleaseDateB.class.getClassLoader());
    }

    public static final Parcelable.Creator<EntryBean> CREATOR = new Parcelable.Creator<EntryBean>() {
        @Override
        public EntryBean createFromParcel(Parcel source) {
            return new EntryBean(source);
        }

        @Override
        public EntryBean[] newArray(int size) {
            return new EntryBean[size];
        }
    };
}
