package sk.skrecyclerview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.skrecyclerview.R;
import sk.skrecyclerview.bean.HomepageEntity;
import sk.skrecyclerview.bean.HomepageItemEntity;


/**
 * Created by SK on 2017-05-05.
 */

public class HomepageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //type
    public static final int TYPE_REMOVE_SLIDER = 101;
    public static final int TYPE_SLIDER = 0;
    public static final int TYPE_TYPE_ACTIVITIES = 1;
    public static final int TYPE_TYPE_SHOPAREA = 2;
    public static final int TYPE_TYPE_Classification = 3;
    public static final int TYPE_TYPE3 = 4;
    public static final int TYPE_HEAD = 5;
    public static final int TYPE_DIVIDER = 6;

    private int ASize = 1; //banner位置
    private int BSize = 0; // 活动位置
    private int CSize = 0; // 商圈位置
    private int DSize = 0; // 分类位置
    private int ESize = 0; // 商户列表位置

    public static final int ROWS = 8;

    private int itemCount = 0;

    private Context context;
    private HomepageEntity entity = new HomepageEntity();
    private ArrayList<HomepageEntity> mDataList = new ArrayList<>();
    private ArrayList<String> transformerList = new ArrayList<String>();


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        switch (viewType) {
            case TYPE_REMOVE_SLIDER:
                return new HolderRemoveSlider(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_item_remove_slider, parent, false));
            case TYPE_SLIDER:
                return new HolderSlider(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_item_banner, parent, false));
            case TYPE_TYPE_ACTIVITIES:
                return new HolderTypeActivities(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_item_activities, parent, false));
            case TYPE_TYPE_SHOPAREA:
                return new HolderTypeShopArea(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_item_shoparea, parent, false));
            case TYPE_TYPE_Classification:
                return new HolderTypeClassification(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_item_classification, parent, false));
            case TYPE_HEAD:
                return new HolderTypeHead(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_item_head, parent, false));
            case TYPE_DIVIDER:
                return new HolderTypeDivider(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_item_divider, parent, false));
            case TYPE_TYPE3:
                return new HolderType2(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_item_list, parent, false));
            default:
                Log.d("error","viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderRemoveSlider) {
            // nothing to do
        } else if (holder instanceof HolderSlider) {
            bindTypeSlider((HolderSlider) holder, position);
        } else if (holder instanceof HolderTypeActivities) {
            bindTypeActivities((HolderTypeActivities) holder, position - ASize);
        } else if (holder instanceof HolderTypeShopArea) {
            bindTypeShopArea((HolderTypeShopArea) holder, position - (BSize + ASize));
        } else if (holder instanceof HolderTypeClassification) {
            bindTypeClassification((HolderTypeClassification) holder, position - (ASize + BSize + CSize));
        } else if (holder instanceof HolderType2) {
            bindType2((HolderType2) holder, position - (ASize + BSize + CSize + DSize + 1));
        } else if (holder instanceof HolderTypeDivider) {
            bindTypeDivider((HolderTypeDivider) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (null != entity.A && entity.A.size() > 0) {
                return TYPE_SLIDER;
            } else {
                return TYPE_REMOVE_SLIDER;
            }
        } else if (0 < position && position < BSize) {
            return TYPE_TYPE_ACTIVITIES;
        } else if (BSize == position) {
            return TYPE_DIVIDER;
        } else if (CSize > 0 && (BSize + ASize) == position) {
            return TYPE_TYPE_SHOPAREA;
        } else if ((BSize + CSize) == position) {
            return TYPE_DIVIDER;
        } else if ((BSize + CSize) < position
                && position < (BSize + CSize + DSize)) {
            return TYPE_TYPE_Classification;
        } else if (position == (BSize + CSize + DSize)) {
            return TYPE_DIVIDER;
        }  else if (position == (BSize + CSize + DSize + 1)) {
            return TYPE_HEAD;
        } else {
            return TYPE_TYPE3;
        }
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public void setData(HomepageEntity entity) {
        this.entity = entity;
        BSize = (null == entity.B || 0 == entity.B.size()) ? 0 : (entity.B.size() + 1);
        CSize = (null == entity.C || 0 == entity.C.size()) ? 0 : (1 + 1);
        DSize = (null == entity.D || 0 == entity.D.size()) ? 0 : (entity.D.size() + 1);
        ESize = (null == entity.E || null == entity.E.list || 0 == entity.E.list.size()) ? 0 : entity.E.list.size();

        // 第一个1是banner，第二个1是热门推荐
        itemCount = ASize + BSize + CSize + DSize + 1 + ESize;
        notifyDataSetChanged();
    }

    private void bindTypeSlider(final HolderSlider holder, final int position) {
        final ArrayList<HomepageItemEntity> bannerEntities = entity.A;
        if (bannerEntities != null && bannerEntities.size() > 0) {

            SliderLoopAdapter sliderLoopAdapter = new SliderLoopAdapter(holder.banner);
            sliderLoopAdapter.setImgs(bannerEntities);
            holder.banner.setAdapter(sliderLoopAdapter);
            holder.banner.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(int position) {
                    Toast.makeText(holder.itemView.getContext(), "OK", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.bannerLayout.setLayoutParams(ll);
        }
    }

    private void bindTypeActivities(final HolderTypeActivities holder, final int position) {
        final ArrayList<HomepageItemEntity> bannerEntities = entity.B;
        if (bannerEntities != null && bannerEntities.size() > 0 && position < bannerEntities.size()) {

            if (bannerEntities.get(position).title.length() > 3) holder.actText.setTextSize(14);
            holder.actText.setText(bannerEntities.get(position).title);

            Glide.with(context)
                    .load(bannerEntities.get(position).img)
                    .placeholder(R.mipmap.song)
                    .animate(android.R.anim.fade_in)
                    .crossFade()
                    .into(holder.actImg);
            holder.actLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.itemView.getContext(), "OK", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.actLayout.setLayoutParams(ll);
        }
    }

    private void bindTypeShopArea(final HolderTypeShopArea holder, final int position) {
        final ArrayList<HomepageItemEntity> bannerEntities = entity.C;
        if (bannerEntities != null && bannerEntities.size() > 0 && position < bannerEntities.size()) {
            ShopAreaAdapter shopAreaAdapter = new ShopAreaAdapter(bannerEntities);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4, GridLayoutManager.HORIZONTAL, false);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 4;
                }
            });
            holder.recyclerView.setLayoutManager(gridLayoutManager);
            holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setAdapter(shopAreaAdapter);

            holder.shopareaLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.itemView.getContext(), "OK", Toast.LENGTH_SHORT).show();
                }
            });

            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.shopareaLayout.setLayoutParams(ll);
        } else {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.shopareaLayout.setLayoutParams(ll);
        }
    }

    private void bindTypeClassification(final HolderTypeClassification holder, final int position) {
        final ArrayList<HomepageItemEntity> bannerEntities = entity.D;
        if (bannerEntities != null && bannerEntities.size() > 0 && position < bannerEntities.size()) {
            holder.actText.setText(bannerEntities.get(position).title);
            Glide.with(context)
                    .load(bannerEntities.get(position).img)
                    .asBitmap()
                    .animate(android.R.anim.fade_in)
                    .placeholder(R.mipmap.song)
                    .into(new BitmapImageViewTarget(holder.actImg) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.actImg.setImageDrawable(circularBitmapDrawable);
                        }
                    });

            holder.clasLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.itemView.getContext(), "OK", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.clasLayout.setLayoutParams(ll);
        }
    }

    private void bindType2(final HolderType2 holder, final int position) {
        final ArrayList<HomepageItemEntity> homeListEntities = entity.E.list;
        if (homeListEntities != null && homeListEntities.size() > 0) {
            holder.text.setText(homeListEntities.get(position).title);
            holder.price.setText(homeListEntities.get(position).price);
            holder.rightMsg.setText(homeListEntities.get(position).rinfo);
            holder.leftMsg.setText(homeListEntities.get(position).linfo);
            Glide.with(context)
                    .load(homeListEntities.get(position).img)
                    .placeholder(R.mipmap.song)
                    .animate(android.R.anim.fade_in)
                    .crossFade()
                    .into(holder.img);

            holder.type2Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.itemView.getContext(), "OK", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void bindTypeDivider(HolderTypeDivider holder, final int position) {
        Log.e("onBindViewHolder", holder + " " +position);
        if (CSize == 0 && (position == (ASize + BSize))) {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.dividerLayout.setLayoutParams(ll);
        }
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = ((RecyclerView) recyclerView).getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case TYPE_REMOVE_SLIDER:
                            return ROWS;
                        case TYPE_SLIDER:
                            return ROWS;
                        case TYPE_TYPE_ACTIVITIES:
                            return 2;
                        case TYPE_TYPE_SHOPAREA:
                            return ROWS;
                        case TYPE_TYPE_Classification:
                            return 2;
                        case TYPE_TYPE3:
                            return ROWS;
                        case TYPE_DIVIDER:
                            return ROWS;
                        default:
                            return ROWS;
                    }
                }
            });
        }
    }

    public class HolderRemoveSlider extends RecyclerView.ViewHolder {

        public HolderRemoveSlider(View itemView) {
            super(itemView);
        }
    }
    public class HolderSlider extends RecyclerView.ViewHolder {
        public RollPagerView banner;
        public View bannerLayout;

        public HolderSlider(View itemView) {
            super(itemView);
            banner = (RollPagerView) itemView.findViewById(R.id.banner);
            bannerLayout = itemView.findViewById(R.id.banner_layout);
        }
    }
    public class HolderTypeActivities extends RecyclerView.ViewHolder {

        @BindView(R.id.act_img)
        public AppCompatImageView actImg;
        @BindView(R.id.act_text)
        public AppCompatTextView actText;
        @BindView(R.id.act_layout)
        public View actLayout;

        public HolderTypeActivities(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public class HolderTypeShopArea extends RecyclerView.ViewHolder {
        @BindView(R.id.act_img)
        public AppCompatImageView actImg;
        @BindView(R.id.act_text)
        public AppCompatTextView actText;
        @BindView(R.id.shoparea_layout)
        public View shopareaLayout;
        @BindView(R.id.recycler_view)
        public RecyclerView recyclerView;
        public HolderTypeShopArea(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public class HolderTypeClassification extends RecyclerView.ViewHolder {
        @BindView(R.id.act_img)
        public AppCompatImageView actImg;
        @BindView(R.id.act_text)
        public AppCompatTextView actText;
        @BindView(R.id.clas_layout)
        public View clasLayout;
        public HolderTypeClassification(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public class HolderType2 extends RecyclerView.ViewHolder {

        @BindView(R.id.item_img_type2)
        public AppCompatImageView img;
        @BindView(R.id.title)
        public AppCompatTextView text;
        @BindView(R.id.price)
        public AppCompatTextView price;
        @BindView(R.id.left_message)
        public AppCompatTextView leftMsg;
        @BindView(R.id.right_message)
        public AppCompatTextView rightMsg;
        @BindView(R.id.type2_layout)
        public View type2Layout;

        public HolderType2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public class HolderTypeHead extends RecyclerView.ViewHolder {
        public HolderTypeHead(View itemView) {
            super(itemView);
        }
    }

    public class HolderTypeDivider extends RecyclerView.ViewHolder {
        @BindView(R.id.divider_layout)
        public View dividerLayout;

        public HolderTypeDivider(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class SliderLoopAdapter extends LoopPagerAdapter {

        ArrayList<HomepageItemEntity> imgs = new ArrayList<>();

        public SliderLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            Glide.with(context)
                    .load(imgs.get(position).img)
                    .placeholder(R.mipmap.timg)
                    .animate(android.R.anim.fade_in)
                    .crossFade()
                    .into(view);

            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.size();
        }

        public void setImgs(ArrayList<HomepageItemEntity> imgs) {
            this.imgs.clear();
            this.imgs = imgs;
            notifyDataSetChanged();
        }
    }

    public class ShopAreaAdapter extends RecyclerView.Adapter {


        private ArrayList<HomepageItemEntity> viewList = new ArrayList();
        public ShopAreaAdapter(ArrayList<HomepageItemEntity> viewList) {
            this.viewList = viewList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShopAreaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shoparea_list, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ShopAreaViewHolder shopAreaViewHolder = (ShopAreaViewHolder) holder;
            shopAreaViewHolder.actText.setText(viewList.get(position).title);
            Glide.with(context)
                    .load(viewList.get(position).img)
                    .placeholder(R.mipmap.song)
                    .animate(android.R.anim.fade_in)
                    .crossFade()
                    .into(shopAreaViewHolder.actImg);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.itemView.getContext(), "OK", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return viewList.size();
        }
    }

    public class ShopAreaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.act_img)
        public AppCompatImageView actImg;
        @BindView(R.id.act_text)
        public AppCompatTextView actText;

        public ShopAreaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
