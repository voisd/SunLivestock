package com.voisd.sun.view.iviews;

/**
 * Created by liucanwen on 16/2/7.
 */
public interface IPageStatusUi {

    public void showPageStatusView(String message);

    public void showPageStatusView(int iconRes, String message);

    public void hidePageStatusView();
}
