package com.cs.phoneguardian.accelerate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.modle.AppInfoDataSource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cs.phoneguardian.accelerate.AccContract.SettingPreseter;
import static com.cs.phoneguardian.accelerate.AccContract.SettingView;

/**
 * Created by Administrator on 2017/7/18.
 */

public class SelectLockAppActivity extends AppCompatActivity implements SettingView {
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.rv_app)
    RecyclerView rvApp;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    private SettingPreseter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lock_app);
        ButterKnife.bind(this);

        SelectLockAppPresenter.getInstance(AppInfoDataSource.getInstance(this), this);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        rvApp.setLayoutManager(mLinearLayoutManager);
        UserAppAdapter mUserAppAdapter = new UserAppAdapter();
        rvApp.setAdapter(mUserAppAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(SettingPreseter presenter) {
        this.mPresenter = presenter;
    }

    public static void startSelectLockAppActivity(Context context) {
        Intent intent = new Intent(context, SelectLockAppActivity.class);
        context.startActivity(intent);
    }

    private class UserAppAdapter extends RecyclerView.Adapter{
        List<AppInfo> userAppList;
        Context mContext;

        UserAppAdapter(Context context) {
            userAppList = new ArrayList<>();
            this.mContext = context;
        }

        public void updateUserAppList(List<AppInfo> list){
            this.userAppList = list;
            this.notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UserAppViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_lock_app_item,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return userAppList.size();
        }

        class UserAppViewHolder extends RecyclerView.ViewHolder{

            public UserAppViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
