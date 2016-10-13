package com.voisd.sun.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bk886.njxzs.R;
import com.voisd.sun.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by voisd on 2016/10/9.
 * 联系方式：531972376@qq.com
 */
public class AboutActivity extends BaseActivity {
    @InjectView(R.id.toolbar_top_layout)
    LinearLayout toolbarTopLayout;
    @InjectView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @InjectView(R.id.toolbar_title)
    TextView toolbarTitle;
    @InjectView(R.id.toolbar_back_btn)
    ImageButton toolbarBackBtn;
    @InjectView(R.id.toolbar_right_title)
    TextView toolbarRightTitle;
    @InjectView(R.id.toolbar_right_btn)
    ImageButton toolbarRightBtn;
    @InjectView(R.id.common_toolbar_line)
    TextView commonToolbarLine;
    @InjectView(R.id.content_tv)
    TextView contentTv;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViewsAndEvents() {

        setToolbarTitle("关于我们");

        String s = "农家小助手APP会提供每日最新的畜牧养殖资讯，帮助养殖户掌握最新养殖风向、技巧。" +
                "APP提供在线交流，会员用户可以在线相互交流自身的养殖心得体会。" +
                "在以后我们会推出更丰富的功能，敬请期待！\n" +
                "如果您在使用软件的过程中，有好的建议，请记得给我们留言噢。\n" +
                "\n" +
                "我们的联系方式\n" +
                "\n" +
                "Q群:578598841\n"+
                "微信:wx334068482";
        contentTv.setText(s);

    }


}
