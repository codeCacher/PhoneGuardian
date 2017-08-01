package com.cs.phoneguardian.intercept.view.InterceptContact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.Contact;
import com.cs.phoneguardian.bean.InterceptContact;
import com.cs.phoneguardian.intercept.InterceptContract;
import com.cs.phoneguardian.intercept.modle.InterceptDataSource;
import com.cs.phoneguardian.intercept.presenter.BlackWhitePresenter;
import com.cs.phoneguardian.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/26.
 */

public class BlackWhiteContactActivity extends AppCompatActivity implements View.OnClickListener,InterceptContract.BlackWhiteBaseView{

    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.bt_black)
    Button btBlack;
    @BindView(R.id.bt_white)
    Button btWhite;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.rl_add)
    RelativeLayout rlAdd;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;

    //request code
    private static final int REQUEST_BLACK_CONTACT = 0;
    private static final int REQUEST_WHITE_CONTACT = 1;
    private static final int REQUEST_BLACK_INPUT = 2;
    private static final int REQUEST_WHITE_INPUT = 3;

    private ArrayList<BlackWhiteFragment> mFragmentList;
    private InterceptContract.BlackWhiteBasePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_white_contact);
        ButterKnife.bind(this);

        com.cs.phoneguardian.intercept.presenter.BlackWhitePresenter.getInstance(this,this, InterceptDataSource.getInstance(this));

        mFragmentList = new ArrayList<>();
        BlackWhiteFragment blackFragment = new BlackWhiteFragment();
        blackFragment.setType(BlackWhiteFragment.TYPE_BLACK);
        blackFragment.setPresenter(mPresenter);
        BlackWhiteFragment whiteFragment = new BlackWhiteFragment();
        whiteFragment.setType(BlackWhiteFragment.TYPE_WHITE);
        whiteFragment.setPresenter(mPresenter);
        mFragmentList.add(blackFragment);
        mFragmentList.add(whiteFragment);

        vpContent.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if(position==BLACK_STATE){
                    showBlack();
                }else {
                    showWhite();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        btBlack.setOnClickListener(this);
        btWhite.setOnClickListener(this);
        rlAdd.setOnClickListener(this);

        //初始化状态
        int state = getIntent().getIntExtra(Constants.KEY_BLACK_WHITE_CONTACT_START_STATE, BLACK_STATE);
        mPresenter.init(state);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void startBlackWhiteContactActivity(Context context, int startState) {
        Intent intent = new Intent(context, BlackWhiteContactActivity.class);
        intent.putExtra(Constants.KEY_BLACK_WHITE_CONTACT_START_STATE, startState);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            ArrayList<Contact> mSelectContactList = data.getParcelableArrayListExtra(Constants.KEY_SELECTED_CONTACT);
            switch (requestCode){
                case REQUEST_BLACK_CONTACT:
                case REQUEST_BLACK_INPUT:
                    mPresenter.addBlackContact(mSelectContactList);
                    break;

                case REQUEST_WHITE_CONTACT:
                case REQUEST_WHITE_INPUT:
                    mPresenter.addWhiteContact(mSelectContactList);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_black:
                showBlack();
                break;

            case R.id.bt_white:
                showWhite();
                break;

            case R.id.rl_add:
                showAddDialog(new OnAddDialogButtonClickedListener() {
                    @Override
                    public void OnContactListClicked() {
                        int currentItem = vpContent.getCurrentItem();
                        if(currentItem==BLACK_STATE){
                            ContactActivity.startContactActivityForResult(BlackWhiteContactActivity.this,BlackWhiteContactActivity.this,REQUEST_BLACK_CONTACT);
                        }else {
                            ContactActivity.startContactActivityForResult(BlackWhiteContactActivity.this,BlackWhiteContactActivity.this,REQUEST_WHITE_CONTACT);
                        }
                    }

                    @Override
                    public void OnInputClicked() {
                        //TODO 手动输入联系人
                    }
                });

                break;
        }
    }

    @Override
    public void showBlack() {
        vpContent.setCurrentItem(BLACK_STATE);
        btBlack.setSelected(true);
        btWhite.setSelected(false);
    }

    @Override
    public void showWhite() {
        vpContent.setCurrentItem(WHITE_STATE);
        btBlack.setSelected(false);
        btWhite.setSelected(true);
    }

    @Override
    public void updateBlackList(List<InterceptContact> list) {
        mFragmentList.get(BLACK_STATE).updateBlackList(list);
    }

    @Override
    public void updateWhiteList(List<InterceptContact> list) {
        mFragmentList.get(WHITE_STATE).updateWhiteList(list);
    }

    @Override
    public void showBlackDeleteDialog(final InterceptContact contact) {
        String name = contact.getName();
        String phoneNumber = contact.getPhoneNumber();

        showDelDialog(name + " " + phoneNumber, new OnDelDialogButtonClickedListener() {
            @Override
            public void OnConfirmed() {
                mPresenter.deleteContact(contact.getPhoneNumber(),BlackWhitePresenter.BLACK_CONTACT);
            }

            @Override
            public void OnCanceled() {

            }
        });
    }

    @Override
    public void showWhiteDeleteDialog(final InterceptContact contact) {
        String name = contact.getName();
        String phoneNumber = contact.getPhoneNumber();

        showDelDialog(name + " " + phoneNumber, new OnDelDialogButtonClickedListener() {
            @Override
            public void OnConfirmed() {
                mPresenter.deleteContact(contact.getPhoneNumber(),BlackWhitePresenter.WHITE_CONTACT);
            }

            @Override
            public void OnCanceled() {

            }
        });
    }

    @Override
    public void setPresenter(BlackWhitePresenter presenter) {
        this.mPresenter = presenter;
    }

    private interface OnDelDialogButtonClickedListener {
        void OnConfirmed();
        void OnCanceled();
    }
    private void showDelDialog(String title, final OnDelDialogButtonClickedListener listener){
        View view = View.inflate(this, R.layout.black_white_remove_dialog, null);
        final AlertDialog addDelDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        Button btCancel = (Button) view.findViewById(R.id.bt_cancel);
        Button btDel = (Button) view.findViewById(R.id.bt_del);

        tvTitle.setText(title);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDelDialog.dismiss();
                listener.OnCanceled();
            }
        });

        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDelDialog.dismiss();
                listener.OnConfirmed();
            }
        });

        addDelDialog.show();
    }

    private interface OnAddDialogButtonClickedListener {
        void OnContactListClicked();
        void OnInputClicked();
    }

    private void showAddDialog(final OnAddDialogButtonClickedListener listener){
        View view = View.inflate(this, R.layout.black_white_add_dialog, null);
        final AlertDialog addDelDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        Button btContact = (Button) view.findViewById(R.id.bt_contact);
        Button btInput = (Button) view.findViewById(R.id.bt_input);

        btContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDelDialog.dismiss();
                listener.OnContactListClicked();
            }
        });

        btInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDelDialog.dismiss();
                listener.OnInputClicked();
            }
        });

        addDelDialog.show();
    }
}
