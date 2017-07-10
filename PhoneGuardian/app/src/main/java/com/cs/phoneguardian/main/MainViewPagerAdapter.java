package com.cs.phoneguardian.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import javax.annotation.CheckForNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Administrator on 2017/7/10.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList;

    public MainViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(checkNotNull(fm));
        this.mFragmentList = checkNotNull(fragmentList);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
