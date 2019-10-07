package com.away.hk01demo;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.away.hk01demo.adapter.RvHorAdapter;
import com.away.hk01demo.adapter.RvVerAdapter;
import com.away.hk01demo.adapter.SearchAdapter;
import com.away.hk01demo.base.BaseActivity;
import com.away.hk01demo.bean.HorListB;
import com.away.hk01demo.bean.entry.EntryBean;
import com.away.hk01demo.bean.lookup.LookupB;
import com.away.hk01demo.common.CommonTextWatcher;
import com.away.hk01demo.table.LookupTable;
import com.away.hk01demo.table.SearchTable;
import com.away.hk01demo.utils.http.CHttpUtils;
import com.away.hk01demo.utils.http.JsonCallback;
import com.away.hk01demo.utils.http.UrlConfig;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okgo.model.Response;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.rv)
    XRecyclerView rv;
    @BindView(R.id.edit_search)
    EditText etSearch;
    @BindView(R.id.rv_search)
    XRecyclerView rvSearch;

    RvHorAdapter horAdapter;
    RvVerAdapter verAdapter;

    SearchAdapter searchAdapter;

    private int times = 1;
    private int itemLimit = 10;

    @Override
    public void windowSetting() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEvent() {
        horList = new ArrayList<>();
        verList = new ArrayList<>();
        verAdapter = new RvVerAdapter(verList, this);
        searchAdapter = new SearchAdapter(searchResults, this);
        initTopList();

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rv.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        rv.setArrowImageView(R.drawable.iconfont_downgrey);
        rv.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        rv.getDefaultFootView().setLoadingDoneHint("加载中...");
        rv.getDefaultFootView().setNoMoreHint("没有更多咯~");
        rv.setLimitNumberToCallLoadMore(2);
        rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verList.clear();
                        requestVerData(itemLimit);
                        if (rv != null) {
                            rv.refreshComplete();
                        }
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                if (times < 10) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            requestVerData(itemLimit);
                            itemLimit = itemLimit + 10;
                            if (rv != null) {
                                rv.loadMoreComplete();
                            }
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            requestVerData(itemLimit);
                            if (rv != null) {
                                rv.setNoMore(true);
                            }
                        }
                    }, 1000);
                }
                times++;
            }
        });
        verAdapter.setClickCallBack(new RvVerAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                verList.remove(pos);
                rv.notifyItemRemoved(verList, pos);
            }
        });
        rv.setAdapter(verAdapter);
        rv.refresh();

        doSearch();
    }

    private void initTopList() {
        View header = LayoutInflater.from(this).inflate(R.layout.rv_header, (ViewGroup) findViewById(android.R.id.content), false);
        rv.addHeaderView(header);
        RecyclerView rvTop = header.findViewById(R.id.rv_top);
        LinearLayoutManager topLayoutManager = new LinearLayoutManager(this);
        topLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTop.setLayoutManager(topLayoutManager);
        horAdapter = new RvHorAdapter(horList, this);
        rvTop.setAdapter(horAdapter);
    }

    private void doSearch() {
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editSearch = (EditText) v;
                String hintStr;
                if (hasFocus) {
                    hintStr = editSearch.getHint().toString();
                    editSearch.setTag(hintStr);
                    editSearch.setHint("");
                } else {
                    hintStr = editSearch.getTag().toString();
                    editSearch.setHint(hintStr);
                }
            }
        });
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                String keyword = etSearch.getText().toString();
                if (TextUtils.isEmpty(keyword)) {
                    toast.toastShow("请先输入搜索内容");
                    return false;
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                requestSearch(keyword);
            }
            return false;
        });
        etSearch.addTextChangedListener(new CommonTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    toast.toastShow("请先输入搜索内容");
                    return;
                }
                requestSearch(keyword);
            }
        });
        bindingRecyclerView(rvSearch);
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setAdapter(searchAdapter);
        rvSearch.setPullRefreshEnabled(false);
    }

    ArrayList<SearchTable> searchResults = new ArrayList<>();

    private void requestSearch(String keyword) {
        List<SearchTable> allSearchTables = LitePal.findAll(SearchTable.class);
        String input = keyword.trim();
        searchResults.clear();
        if (TextUtils.isEmpty(input)) {
            searchResults.addAll(allSearchTables);
        } else {
            for (int i = 0; i < allSearchTables.size(); i++) {
                SearchTable searchTable = allSearchTables.get(i);
                String title = searchTable.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    if (title.contains(input)) {
                        if (!searchResults.contains(searchTable)) {
                            searchResults.add(searchTable);
                        }
                    }
                }
                String category = searchTable.getCategory();
                if (!TextUtils.isEmpty(category)) {
                    if (category.contains(input)) {
                        if (!searchResults.contains(searchTable)) {
                            searchResults.add(searchTable);
                        }
                    }
                }
            }
        }
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestHorData();
        requestVerData();
    }

    private void requestLookup(String appId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", appId);
        CHttpUtils.getInstance().requestDataFromServer(UrlConfig.URL_LOOKUP_LIST, params, this, new JsonCallback<LookupB>(LookupB.class) {
            @Override
            public void onSuccess(Response<LookupB> response) {
                if (isActivityFinish) return;
                if (response == null || response.body() == null) return;
                LookupB lookupB = response.body();
                if (lookupB.resultCount == 1 && lookupB.results.size() != 0) {
                    double averageUserRating = lookupB.results.get(0).averageUserRating;
                    int userRatingCount = lookupB.results.get(0).userRatingCount;

                    LookupTable lookupTable = new LookupTable();
                    List<LookupTable> lookupTables = LitePal.findAll(LookupTable.class);
                    for (int i = 0; i < lookupTables.size(); i++) {
                        if (lookupTables.get(i).getAppId() == appId) {
                            lookupTables.get(i).delete();
                        }
                    }
                    lookupTable.setAppId(appId);
                    lookupTable.setAverageUserRating(averageUserRating);
                    lookupTable.setUserRatingCount(userRatingCount);
                    lookupTable.save();
                }
            }

            @Override
            public void onError(Response<LookupB> response) {
                super.onError(response);
                if (isActivityFinish) return;
            }
        });
    }

    ArrayList<EntryBean> horList;
    ArrayList<EntryBean> verList;
    ArrayList<EntryBean> datas;

    private void requestHorData() {
        CHttpUtils.getInstance().requestDataFromServer(UrlConfig.URL_HOR_LIST, null, this, new JsonCallback<HorListB>(HorListB.class) {
            @Override
            public void onSuccess(Response<HorListB> response) {
                if (isActivityFinish) return;
                if (response == null || response.body() == null) return;
                HorListB body = response.body();
                if (body.feed.entry.size() == 0 || body.feed.entry == null) return;
                horList.clear();
                if (body.feed.entry != null && body.feed.entry.size() > 0) {
                    List<SearchTable> searchTables = LitePal.findAll(SearchTable.class);
                    for (int i = 0; i < body.feed.entry.size(); i++) {
                        SearchTable searchTable = new SearchTable();
                        for (int j = 0; j < searchTables.size(); j++) {
                            if (body.feed.entry.get(i).title.label.equals(searchTables.get(j).getTitle())) {
                                searchTables.get(j).delete();
                            }
                        }
                        searchTable.setTitle(body.feed.entry.get(i).title.label);
                        searchTable.setCategory(body.feed.entry.get(i).category.attributes.label);
                        searchTable.setIconUrl(body.feed.entry.get(i).link.get(1).attributes.href);
                        searchTable.save();
                    }

                    horList.addAll(body.feed.entry);
                    horAdapter.notifyDataSetChanged();

                } else {
                    horAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Response<HorListB> response) {
                super.onError(response);
                if (isActivityFinish) return;
                horList.clear();
                horAdapter.notifyDataSetChanged();
            }
        });
    }

    private void requestVerData(int limit) {
        CHttpUtils.getInstance().requestDataFromServer(UrlConfig.URL_VER_LIST + "limit=" + limit + "/json", null, this, new JsonCallback<HorListB>(HorListB.class) {
            @Override
            public void onSuccess(Response<HorListB> response) {
                if (isActivityFinish) return;
                if (response == null || response.body() == null) return;
                HorListB body = response.body();
                if (body.feed.entry.size() == 0 || body.feed.entry == null) return;
                verList.clear();
                if (body.feed.entry != null && body.feed.entry.size() > 0) {
                    verList.addAll(body.feed.entry);
                    List<String> idStrs = new ArrayList<>();
                    for (int i = 0; i < body.feed.entry.size(); i++) {
                        idStrs.add("" + body.feed.entry.get(i).link.get(0).attributes.href);
                    }
                    verAdapter.notifyDataSetChanged();
                } else {
                    verAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(Response<HorListB> response) {
                super.onError(response);
                if (isActivityFinish) return;
                verList.clear();
                verAdapter.notifyDataSetChanged();
            }
        });
    }

    private String getIdStr(String idStr) {
        String result = "";
        String regex = "/id(.*?)uo=";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(idStr);
        while (m.find()) {
            System.out.println(m.group(1));
            result = m.group(1);
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    List<String> ids = new ArrayList<>();

    private void requestVerData() {
        datas = new ArrayList<>();
        CHttpUtils.getInstance().requestDataFromServer(UrlConfig.URL_VER_LIST + "limit=" + 100 + "/json", null, this, new JsonCallback<HorListB>(HorListB.class) {
            @Override
            public void onSuccess(Response<HorListB> response) {
                if (isActivityFinish) return;
                if (response == null || response.body() == null) return;
                HorListB body = response.body();
                if (body.feed.entry.size() == 0 || body.feed.entry == null) return;
                datas.clear();
                List<SearchTable> searchTables = LitePal.findAll(SearchTable.class);
                if (body.feed.entry != null && body.feed.entry.size() > 0) {
                    for (int i = 0; i < body.feed.entry.size(); i++) {
                        SearchTable searchTable = new SearchTable();
                        for (int j = 0; j < searchTables.size(); j++) {
                            if (body.feed.entry.get(i).title.label.equals(searchTables.get(j).getTitle())) {
                                searchTables.get(j).delete();
                            }
                        }
                        searchTable.setTitle(body.feed.entry.get(i).title.label);
                        searchTable.setCategory(body.feed.entry.get(i).category.attributes.label);
                        searchTable.setIconUrl(body.feed.entry.get(i).link.get(1).attributes.href);
                        searchTable.save();
                    }
                    datas.addAll(body.feed.entry);

                    for (int i = 0; i < body.feed.entry.size(); i++) {
                        ids.add(getIdStr(body.feed.entry.get(i).link.get(0).attributes.href));
                    }
                    for (int i = 0; i < ids.size(); i++) {
                        MCallBack(i, new MCallBack() {
                            @Override
                            public void BackCall(int pos) {
                                requestLookup(ids.get(pos));
                            }
                        });
                    }
                    List<LookupTable> lookupTables = LitePal.findAll(LookupTable.class);
                    verAdapter.setData(lookupTables);
                }

            }

            @Override
            public void onError(Response<HorListB> response) {
                super.onError(response);
                if (isActivityFinish) return;
                datas.clear();
            }
        });
    }

    public static void MCallBack(int pos, MCallBack call) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                call.BackCall(pos);
            }
        }).start();
    }

    interface MCallBack {
        public void BackCall(int pos);
    }
}
