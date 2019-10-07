package com.away.hk01demo.table;

import org.litepal.crud.LitePalSupport;

/**
 * appid--星星评分+用户量
 */
public class LookupTable extends LitePalSupport {
    public String appId;
    public double averageUserRating;//评分
    public int userRatingCount;//用户量

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public double getAverageUserRating() {
        return averageUserRating;
    }

    public void setAverageUserRating(double averageUserRating) {
        this.averageUserRating = averageUserRating;
    }

    public int getUserRatingCount() {
        return userRatingCount;
    }

    public void setUserRatingCount(int userRatingCount) {
        this.userRatingCount = userRatingCount;
    }
}
