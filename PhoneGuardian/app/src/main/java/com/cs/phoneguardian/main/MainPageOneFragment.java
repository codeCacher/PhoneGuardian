package com.cs.phoneguardian.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cs.phoneguardian.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/9.
 * 主界面功能列表第一页
 */

public class MainPageOneFragment extends Fragment implements FunctionItemAdapter.OnItemClickedListener {
    @BindView(R.id.rv_grid)
    RecyclerView rvGrid;
    private GridLayoutManager mGridLayoutManager;
    private FunctionItemAdapter mFunctionItemAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        ButterKnife.bind(this, view);

        initFuncGrid();
        setOnClickListener();

        return view;
    }

    /**
     * 初始化功能列表
     */
    private void initFuncGrid(){
        String[] nameList = new String[]{"加速优化","空间清理","骚扰拦截","省电管理","流量管理","通知中心"};
        Integer[] pictureIdList = new Integer[]{R.drawable.accelerate,R.drawable.clean,R.drawable.accelerate,
                R.drawable.accelerate,R.drawable.accelerate,R.drawable.accelerate};
        mFunctionItemAdapter = new FunctionItemAdapter(getContext(), nameList, pictureIdList);
        mGridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvGrid.setAdapter(mFunctionItemAdapter);
        rvGrid.setLayoutManager(mGridLayoutManager);
        rvGrid.addItemDecoration(new FuncItemDecoration(getContext(),1));
    }

    private void setOnClickListener() {
        mFunctionItemAdapter.setOnItemClickedLisener(this);
    }

    @Override
    public void OnClicke(int position) {
        switch (position){
            case 0:
                Toast.makeText(getContext(),position+"",Toast.LENGTH_SHORT).show();
                break;

            case 1:
                Toast.makeText(getContext(),position+"",Toast.LENGTH_SHORT).show();

                break;

            case 2:
                Toast.makeText(getContext(),position+"",Toast.LENGTH_SHORT).show();

                break;

            case 3:
                Toast.makeText(getContext(),position+"",Toast.LENGTH_SHORT).show();

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
