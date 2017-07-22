package com.cs.phoneguardian.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.accelerate.AccActivity;
import com.cs.phoneguardian.applock.AppLockActivity;
import com.cs.phoneguardian.clearcache.ClearCacheActivity;
import com.cs.phoneguardian.guardian.activity.GuardActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Administrator on 2017/7/9.
 * 主界面功能列表第一页
 */

public class MainViewPagerFragment extends Fragment implements FunctionItemAdapter.OnItemClickedListener{
    @BindView(R.id.rv_grid)
    RecyclerView rvGrid;
    private GridLayoutManager mGridLayoutManager;
    private FunctionItemAdapter mFunctionItemAdapter;
    private String[] mNameList;
    private Integer[] mPictureIdList;

    public MainViewPagerFragment() {
        mNameList = new String[]{};
        mPictureIdList = new Integer[]{};
    }

    public void setArgument(String[] nameList,Integer[] pictureIdList) {
        this.mNameList = checkNotNull(nameList);
        this.mPictureIdList = checkNotNull(pictureIdList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        ButterKnife.bind(this, view);

        initFuncGrid();
        setFuncItemOnClickListener();

        return view;
    }

    /**
     * 初始化功能列表
     */
    public void initFuncGrid(){
        mFunctionItemAdapter = new FunctionItemAdapter(getContext(), mNameList, mPictureIdList);
        mGridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvGrid.setAdapter(mFunctionItemAdapter);
        rvGrid.setLayoutManager(mGridLayoutManager);
        rvGrid.addItemDecoration(new FuncItemDecoration(getContext(),1));
    }

    public void setFuncItemOnClickListener() {
        mFunctionItemAdapter.setOnItemClickedLisener(this);
    }

    @Override
    public void OnClicke(int position) {
        switch (position){
            case 0:
                AccActivity.startAccAcitvity(getContext());
                break;

            case 1:
                ClearCacheActivity.startClearCacheActivity(getContext());
                break;

            case 2:
                GuardActivity.startGuardAcitvity(getContext());
                break;

            case 3:
                AppLockActivity.startAppLockActivity(getContext());
                break;

            case 4:
                Toast.makeText(getContext(),position+"",Toast.LENGTH_SHORT).show();

                break;

            case 5:
                Toast.makeText(getContext(),position+"",Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
