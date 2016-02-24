package com.baoluo.community.ui.customview;


import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 *
 * Created by tao.lai on 2015/11/10 0010.
 */

public class Pull2RefreshListView extends PullToRefreshListView {

    public Pull2RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public Pull2RefreshListView(Context context, com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
                                com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle style) {
        super(context, mode, style);
        // TODO Auto-generated constructor stub
    }

    public Pull2RefreshListView(Context context, com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
        super(context, mode);
        // TODO Auto-generated constructor stub
    }

    public Pull2RefreshListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected ListView createListView(Context context, AttributeSet attrs) {
        ListView listView = super.createListView(context, attrs);
        return listView;
    }

}
