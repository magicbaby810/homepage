package sk.srecyclerview_library.listener;

/**
 * Created by SK on 2017-01-24.
 */

import android.view.View;

/**
 * 加载更多FooterView需要实现的接口
 */
public interface ILoadMoreFooter {
    /**
     * 状态回调，回复初始设置
     */
    void onReset();

    /**
     * 状态回调，加载中
     */
    void onLoading();

    /**
     * 状态回调，加载完成
     */
    void onComplete();

    /**
     * 状态回调，已全部加载完成
     */
    void onNoMore();

    /**
     * 加载更多的View
     */
    View getFootView();
}
