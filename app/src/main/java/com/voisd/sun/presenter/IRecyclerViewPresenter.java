package com.voisd.sun.presenter;

import android.content.Context;

import java.util.Map;

/**
 * 公用RecyclerView Present 接口
 * 适用于RecyclerView 列表实现
 * Created by voisd on 16/5/23.
 */
public interface IRecyclerViewPresenter {

    /**
     * 请求数据
     * @param eventTag
     * @param context
     * @param url
     * @param map
     */
    void loadData(int eventTag, Context context, String url, Map<String, String> map);
}
