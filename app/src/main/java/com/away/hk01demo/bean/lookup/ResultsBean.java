package com.away.hk01demo.bean.lookup;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class ResultsBean implements Parcelable {
    public boolean isGameCenterEnabled;
    public String artworkUrl60;
    public String artworkUrl512;
    public String artworkUrl100;
    public String artistViewUrl;
    public String kind;
    public double averageUserRatingForCurrentVersion;
    public String trackCensoredName;
    public String fileSizeBytes;
    public String contentAdvisoryRating;
    public int userRatingCountForCurrentVersion;
    public String trackViewUrl;
    public String trackContentRating;
    public String releaseDate;
    public String currentVersionReleaseDate;
    public String sellerName;
    public int primaryGenreId;
    public String currency;
    public String wrapperType;
    public String version;
    public String minimumOsVersion;
    public int trackId;
    public int artistId;
    public String artistName;
    public int price;
    public String description;
    public String bundleId;
    public String trackName;
    public boolean isVppDeviceBasedLicensingEnabled;
    public String formattedPrice;
    public String primaryGenreName;
    public double averageUserRating;//用户评分
    public int userRatingCount;//
    public List<String> screenshotUrls;
    public List<String> ipadScreenshotUrls;
    public List<String> supportedDevices;
    public List<String> features;
    public List<String> advisories;
    public List<String> languageCodesISO2A;
    public List<String> genres;
    public List<String> genreIds;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isGameCenterEnabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.artworkUrl60);
        dest.writeString(this.artworkUrl512);
        dest.writeString(this.artworkUrl100);
        dest.writeString(this.artistViewUrl);
        dest.writeString(this.kind);
        dest.writeDouble(this.averageUserRatingForCurrentVersion);
        dest.writeString(this.trackCensoredName);
        dest.writeString(this.fileSizeBytes);
        dest.writeString(this.contentAdvisoryRating);
        dest.writeInt(this.userRatingCountForCurrentVersion);
        dest.writeString(this.trackViewUrl);
        dest.writeString(this.trackContentRating);
        dest.writeString(this.releaseDate);
        dest.writeString(this.currentVersionReleaseDate);
        dest.writeString(this.sellerName);
        dest.writeInt(this.primaryGenreId);
        dest.writeString(this.currency);
        dest.writeString(this.wrapperType);
        dest.writeString(this.version);
        dest.writeString(this.minimumOsVersion);
        dest.writeInt(this.trackId);
        dest.writeInt(this.artistId);
        dest.writeString(this.artistName);
        dest.writeInt(this.price);
        dest.writeString(this.description);
        dest.writeString(this.bundleId);
        dest.writeString(this.trackName);
        dest.writeByte(this.isVppDeviceBasedLicensingEnabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.formattedPrice);
        dest.writeString(this.primaryGenreName);
        dest.writeDouble(this.averageUserRating);
        dest.writeInt(this.userRatingCount);
        dest.writeStringList(this.screenshotUrls);
        dest.writeStringList(this.ipadScreenshotUrls);
        dest.writeStringList(this.supportedDevices);
        dest.writeStringList(this.features);
        dest.writeStringList(this.advisories);
        dest.writeStringList(this.languageCodesISO2A);
        dest.writeStringList(this.genres);
        dest.writeStringList(this.genreIds);
    }

    public ResultsBean() {
    }

    protected ResultsBean(Parcel in) {
        this.isGameCenterEnabled = in.readByte() != 0;
        this.artworkUrl60 = in.readString();
        this.artworkUrl512 = in.readString();
        this.artworkUrl100 = in.readString();
        this.artistViewUrl = in.readString();
        this.kind = in.readString();
        this.averageUserRatingForCurrentVersion = in.readDouble();
        this.trackCensoredName = in.readString();
        this.fileSizeBytes = in.readString();
        this.contentAdvisoryRating = in.readString();
        this.userRatingCountForCurrentVersion = in.readInt();
        this.trackViewUrl = in.readString();
        this.trackContentRating = in.readString();
        this.releaseDate = in.readString();
        this.currentVersionReleaseDate = in.readString();
        this.sellerName = in.readString();
        this.primaryGenreId = in.readInt();
        this.currency = in.readString();
        this.wrapperType = in.readString();
        this.version = in.readString();
        this.minimumOsVersion = in.readString();
        this.trackId = in.readInt();
        this.artistId = in.readInt();
        this.artistName = in.readString();
        this.price = in.readInt();
        this.description = in.readString();
        this.bundleId = in.readString();
        this.trackName = in.readString();
        this.isVppDeviceBasedLicensingEnabled = in.readByte() != 0;
        this.formattedPrice = in.readString();
        this.primaryGenreName = in.readString();
        this.averageUserRating = in.readDouble();
        this.userRatingCount = in.readInt();
        this.screenshotUrls = in.createStringArrayList();
        this.ipadScreenshotUrls = in.createStringArrayList();
        this.supportedDevices = in.createStringArrayList();
        this.features = in.createStringArrayList();
        this.advisories = in.createStringArrayList();
        this.languageCodesISO2A = in.createStringArrayList();
        this.genres = in.createStringArrayList();
        this.genreIds = in.createStringArrayList();
    }

    public static final Creator<ResultsBean> CREATOR = new Creator<ResultsBean>() {
        @Override
        public ResultsBean createFromParcel(Parcel source) {
            return new ResultsBean(source);
        }

        @Override
        public ResultsBean[] newArray(int size) {
            return new ResultsBean[size];
        }
    };
}
