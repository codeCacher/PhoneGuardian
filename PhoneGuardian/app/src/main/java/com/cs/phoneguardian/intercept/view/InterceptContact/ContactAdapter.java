package com.cs.phoneguardian.intercept.view.InterceptContact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.InterceptContact;
import com.cs.phoneguardian.intercept.InterceptContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/23.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    public static final int TYPE_BLACK = 0;
    public static final int TYPE_WHITE = 1;
    private final int mType;

    private Context mContext;
    InterceptContract.BlackWhiteBasePresenter mPresenter;
    List<InterceptContact> mContactList;

    public ContactAdapter(Context context, InterceptContract.BlackWhiteBasePresenter presenter,int type) {
        mContactList = new ArrayList<>();
        this.mContext = context;
        this.mPresenter = presenter;
        this.mType = type;
    }

    public void updateList(List<InterceptContact> list) {
        this.mContactList = list;
        notifyDataSetChanged();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(mContext).inflate(R.layout.black_white_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        InterceptContact contact = mContactList.get(position);
        String name = contact.getName();
        String phoneNumber = contact.getPhoneNumber();

        holder.tvName.setText(name);
        holder.tvText.setText(phoneNumber);
        final int fPosition = position;
        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mType == TYPE_BLACK){
                    mPresenter.selectBlackContact(fPosition);
                } else {
                    mPresenter.selectWhiteContact(fPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_text)
        TextView tvText;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
