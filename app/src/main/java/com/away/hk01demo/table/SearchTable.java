package com.away.hk01demo.table;

import org.litepal.crud.LitePalSupport;

public class SearchTable extends LitePalSupport {
    private String title;//包含名称+作者
    private String category;//分类
    private String iconUrl;//头像

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
