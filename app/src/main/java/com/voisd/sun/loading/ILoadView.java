package com.voisd.sun.loading;

import android.view.View;

/**
 * 加载更多
 * Created by voisd on 16/5/23.
 */
public interface ILoadView {

    public abstract View inflate();

    public abstract void showLoadingView(View parentView);

    public abstract void showErrorView(View parentView);

    public abstract void showFinishView(View parentView);
}
