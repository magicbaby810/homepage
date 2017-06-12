package sk.srecyclerview_library;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import sk.srecyclerview_library.listener.OnItemClickListener;
import sk.srecyclerview_library.listener.OnItemLongClickListener;

/**
 * Created by SK on 2017-06-09.
 */

public class SRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER_VIEW = 10001;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;


    /**
     * RecyclerView使用的，真正的Adapter
     */
    private RecyclerView.Adapter mInnerAdapter;

    private ArrayList<View> mFooterViews = new ArrayList<>();


    public SRecyclerViewAdapter(RecyclerView.Adapter innerAdapter) {
        this.mInnerAdapter = innerAdapter;
    }

    public RecyclerView.Adapter getInnerAdapter() {
        return mInnerAdapter;
    }

    public void addFooterView(View view) {
        if (view == null) {
            throw new RuntimeException("footer is null");
        }
        if (getFooterViewsCount() > 0) {
            removeFooterView(getFooterView());
        }
        mFooterViews.add(view);
    }

    /**
     * 返回第一个FootView
     * @return
     */
    public View getFooterView() {
        return  getFooterViewsCount()>0 ? mFooterViews.get(0) : null;
    }

    public void removeFooterView(View view) {
        mFooterViews.remove(view);
        this.notifyDataSetChanged();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    public boolean isFooter(int position) {
        int lastPosition = getItemCount() - 1;
        return getFooterViewsCount() > 0 && position >= lastPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER_VIEW) {
            return new SRecyclerViewAdapter.ViewHolder(mFooterViews.get(0));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int adapterCount;
        if (mInnerAdapter != null) {
            adapterCount = mInnerAdapter.getItemCount();
            if (position < adapterCount) {
                mInnerAdapter.onBindViewHolder(holder, position);

                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener()  {
                        @Override
                        public void onClick(View v)
                        {
                            mOnItemClickListener.onItemClick(holder.itemView, position);
                        }
                    });
                }

                if (mOnItemLongClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v)
                        {
                            mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                            return true;
                        }
                    });
                }

            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder,position);
        } else {
            int adapterCount;
            if (mInnerAdapter != null) {
                adapterCount = mInnerAdapter.getItemCount();
                if (position < adapterCount) {
                    mInnerAdapter.onBindViewHolder(holder, position, payloads);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mInnerAdapter != null) {
            return getFooterViewsCount() + mInnerAdapter.getItemCount();
        } else {
            return getFooterViewsCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position)) {
            return TYPE_FOOTER_VIEW;
        }
        int adapterCount;
        if (mInnerAdapter != null) {
            adapterCount = mInnerAdapter.getItemCount();
            if (position < adapterCount) {
                return mInnerAdapter.getItemViewType(position);
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        if (mInnerAdapter != null) {
            int adapterCount = mInnerAdapter.getItemCount();
            if (position < adapterCount) {
                return mInnerAdapter.getItemId(position);
            }
        }
        return -1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isFooter(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            if(isFooter(holder.getLayoutPosition())) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        mInnerAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewRecycled(holder);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.mOnItemLongClickListener = itemLongClickListener;
    }
}
