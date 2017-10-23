package sk.skrecyclerview.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;


import butterknife.BindView;
import sk.skrecyclerview.R;
import sk.skrecyclerview.bean.Entity;
import sk.skrecyclerview.bean.ListResponse;
import sk.skrecyclerview.utils.AppUtils;
import sk.srecyclerview_library.SRecyclerView;
import sk.srecyclerview_library.SRecyclerViewAdapter;
import sk.srecyclerview_library.listener.OnLoadMoreListener;
import sk.srecyclerview_library.utils.ProgressStyle;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;


public abstract class BaseListFragment<T extends Entity, D extends ListResponse> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    /**
     * 每一页展示多少条数据
     */
    protected int mCurrentPage = 0;
    protected int totalPage = 0;
    protected final int REQUEST_COUNT = 10;

    @BindView(R.id.recycler_view)
    protected SRecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.root_view)
    protected LinearLayout mRootView;
    @BindView(R.id.dynamic_footer_view)
    protected LinearLayout mDynamicFooterView;

    @BindView(R.id.top_btn)
    protected Button toTopBtn;

    protected SRecyclerViewAdapter mRecyclerViewAdapter;

    protected boolean isRequestInProcess = false;
    protected boolean mIsStart = false;


    @Override
    protected int getLayoutID() {
        return R.layout.fragment_pull_refresh_recyclerview;
    }

    protected abstract RecyclerView.Adapter getAdapter();


    @Override
    public void onPause() {
        Glide.with(getActivity()).pauseRequests();
        super.onPause();
    }

    @Override
    public void onResume() {
        Glide.with(getActivity()).resumeRequests();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView(View view) {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_orange_light, android.R.color.holo_red_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setProgressViewOffset(true, 0, AppUtils.dip2px(getActivity(), 24));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(view.findViewById(R.id.empty_view));
        initLayoutManager();
        mRecyclerViewAdapter = new SRecyclerViewAdapter(getAdapter());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (isRequestInProcess) return;

                if (mCurrentPage < totalPage) {
                    mCurrentPage++;
                    isRequestInProcess = true;
                    // loading more  因为数据和加载更多的数据是不一样的，
                    // 所以建议把这个接口分成2个，一个用来请求首页复杂布局的数据，
                    // 另一个请求单列表数据
                    //loadMoreData();
                } else {
                    mRecyclerView.setNoMore(true);
                }

            }
        });

        mRecyclerView.setLScrollListener(new SRecyclerView.LScrollListener() {

            @Override
            public void onScrollUp() {
                // 滑动时隐藏float button
                if (toTopBtn.getVisibility() == View.VISIBLE) {
                    toTopBtn.setVisibility(View.GONE);
                    animate(toTopBtn, R.anim.floating_action_button_hide);
                }
            }

            @Override
            public void onScrollDown() {
                if (toTopBtn.getVisibility() != View.VISIBLE) {
                    toTopBtn.setVisibility(View.VISIBLE);
                    animate(toTopBtn, R.anim.floating_action_button_show);
                }
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                if (distanceY == 0) {
                    toTopBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(int state) {
                switch (state) {
                    case SCROLL_STATE_IDLE:
                        Glide.with(getActivity()).resumeRequests();
                        break;
                    case SCROLL_STATE_DRAGGING:
                    case SCROLL_STATE_SETTLING:
                        Glide.with(getActivity()).pauseRequests();
                        break;
                }
            }

        });

        toTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(0);
                toTopBtn.setVisibility(View.GONE);
            }
        });

        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.gray_text_89, R.color.gray_text_89, R.color.app_bg);
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (isRequestInProcess) {
            return;
        }
        mCurrentPage = 0;
        mRecyclerView.mRefreshing = true;
        requestData();
    }

    protected abstract void requestData();

    protected boolean requestDataIfViewCreated() {
        return true;
    }


    private void animate(View view, int anim) {
        if (anim != 0) {
            Animation a = AnimationUtils.loadAnimation(view.getContext(), anim);
            view.startAnimation(a);
        }
    }

    /**
     * 设置顶部加载完毕的状态
     */
    protected void setSwipeRefreshLoadedState() {
        if(null != mRecyclerView) {
            mRecyclerView.refreshComplete(REQUEST_COUNT);
        }
    }

    // 完成刷新
    protected void executeOnLoadFinish() {
        setSwipeRefreshLoadedState();
        isRequestInProcess = false;
        mIsStart = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected abstract void initLayoutManager();

    protected boolean isHaveBanner() {
        return false;
    }

    protected void refreshBanner() {
    }

    protected void onRefreshView() {

    }


    protected boolean needShowEmptyNoData() {
        return true;
    }

    /**
     * 没有数据时设置提示内容
     * @return
     */
    protected String getNoDataTip() {
        return "";
    }

    /**
     * 没有数据时设置图片
     * @return
     */
    protected int getEmptyResId() {
        return 0;
    }

}
