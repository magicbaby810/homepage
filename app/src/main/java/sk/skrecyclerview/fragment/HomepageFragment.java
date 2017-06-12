package sk.skrecyclerview.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import sk.skrecyclerview.adapter.HomepageAdapter;
import sk.skrecyclerview.base.BaseListFragment;
import sk.skrecyclerview.bean.HomepageEntity;
import sk.skrecyclerview.bean.HomepageItemEntity;
import sk.skrecyclerview.bean.ShopAreaEntity;

import static sk.skrecyclerview.adapter.HomepageAdapter.ROWS;

/**
 * Created by SK on 2017-06-09.
 */

public class HomepageFragment extends BaseListFragment {

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new HomepageAdapter();
    }

    @Override
    protected void requestData() {
        HomepageEntity homepageEntity = new HomepageEntity();
        ArrayList<HomepageItemEntity> aList = new ArrayList<>();
        for (int i = 0; i < 4; i ++) {
            HomepageItemEntity aEntity = new HomepageItemEntity();
            aEntity.title = "sdad";
            aEntity.url = "https://www.baidu.com/";
            aEntity.img = "";
            aList.add(aEntity);
        }
        homepageEntity.A = aList;

        ArrayList<HomepageItemEntity> bList = new ArrayList<>();
        for (int i = 0; i < 4; i ++) {
            HomepageItemEntity bEntity = new HomepageItemEntity();
            bEntity.title = "sdad";
            bEntity.url = "https://www.baidu.com/";
            bEntity.img = "";
            bList.add(bEntity);
        }
        homepageEntity.B = bList;

        ArrayList<HomepageItemEntity> cList = new ArrayList<>();
        for (int i = 0; i < 4; i ++) {
            HomepageItemEntity cEntity = new HomepageItemEntity();
            cEntity.title = "sdad";
            cEntity.url = "https://www.baidu.com/";
            cEntity.img = "";
            cList.add(cEntity);
        }
        homepageEntity.C = cList;

        ArrayList<HomepageItemEntity> dList = new ArrayList<>();
        for (int i = 0; i < 8; i ++) {
            HomepageItemEntity dEntity = new HomepageItemEntity();
            dEntity.title = "sdad";
            dEntity.url = "https://www.baidu.com/";
            dEntity.img = "";
            dList.add(dEntity);
        }
        homepageEntity.D = dList;

        ShopAreaEntity shopAreaEntity = new ShopAreaEntity();
        shopAreaEntity.totalpage = 2;
        shopAreaEntity.page = 1;
        shopAreaEntity.list = new ArrayList<>();
        for (int i = 0; i < 14; i ++) {
            HomepageItemEntity eEntity = new HomepageItemEntity();
            eEntity.title = "sdad";
            eEntity.url = "https://www.baidu.com/";
            eEntity.img = "";
            eEntity.rinfo = "asdwerw";
            eEntity.linfo = "fsfsfs";
            shopAreaEntity.list.add(eEntity);
        }
        homepageEntity.E = shopAreaEntity;

        ((HomepageAdapter) mRecyclerViewAdapter.getInnerAdapter()).setData(homepageEntity);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void initLayoutManager() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), ROWS, GridLayoutManager.VERTICAL, false));
    }


}
