package com.cs.phoneguardian.intercept.view.InterceptContact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.Contact;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.ContactsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21.
 */

public class ContactActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search_key)
    EditText etSearchKey;
    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_confirm)
    Button btConfirm;

    private List<Contact> mContactList;
    private List<Contact> mFiltedContactList;
    private ArrayList<Contact> mSelectContactList;
    private LinearLayoutManager mLinearLayoutManager;
    private ContactAdapter mContactAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_mult);
        ButterKnife.bind(this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        rvContacts.setLayoutManager(mLinearLayoutManager);
        mContactAdapter = new ContactAdapter(this);
        rvContacts.setAdapter(mContactAdapter);

        mFiltedContactList = new ArrayList<>();
        mContactList = ContactsUtils.readContacts(this);
        mContactAdapter.updateContactList(mContactList);

        btCancel.setOnClickListener(this);
        btConfirm.setOnClickListener(this);

        etSearchKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mContactAdapter.updateContactList(mContactList);
                } else {
                    mFiltedContactList.clear();
                    for (Contact contact : mContactList) {
                        String name = contact.getName();
                        if (name == null) {
                            continue;
                        }
                        if (name.startsWith(s.toString())) {
                            mFiltedContactList.add(contact);
                        }
                    }
                    mContactAdapter.updateContactList(mFiltedContactList);
                }

            }
        });
    }

    public static void startContactActivity(Context context) {
        Intent intent = new Intent(context, ContactActivity.class);
        context.startActivity(intent);
    }

    public static void fragmentStartContactActivityForResult(Context context, Fragment fragment, int requestCode) {
        Intent intent = new Intent(context, ContactActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startContactActivityForResult(Context context, Activity activity, int requestCode) {
        Intent intent = new Intent(context, ContactActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                Intent intent = new Intent();
                mSelectContactList = new ArrayList<>();
                for (Contact c : mContactList) {
                    if (c.isSelected()){
                        mSelectContactList.add(c);
                    }
                }
                intent.putParcelableArrayListExtra(Constants.KEY_SELECTED_CONTACT,mSelectContactList);
                setResult(0,intent);
                this.finish();
                break;

            case R.id.bt_cancel:
                this.finish();
                break;
        }
    }

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

        private List<Contact> mContactList;
        private Context mContext;

        ContactAdapter(Context context) {
            mContactList = new ArrayList<>();
            mContext = context;
        }

        void updateContactList(List<Contact> list) {
            this.mContactList = list;
            notifyDataSetChanged();
        }

        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContactViewHolder(LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ContactViewHolder holder, int position) {
            final Contact contact = mContactList.get(position);
            String name = contact.getName();
            String phoneNumber = contact.getPhoneNumber();
            final boolean selected = contact.isSelected();
            holder.tvUserName.setText(name);
            holder.tvNumber.setText(phoneNumber);
            holder.ivSelect.setVisibility(View.VISIBLE);
            holder.ivSelect.setSelected(selected);

            if (TextUtils.isEmpty(name)) {
                contact.setName(phoneNumber);
                holder.tvUserName.setText(contact.getName());
            }

            holder.rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ivSelect.setSelected(!contact.isSelected());
                    contact.setSelected(!contact.isSelected());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mContactList.size();
        }

        class ContactViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_title)
            ImageView ivTitle;
            @BindView(R.id.tv_user_name)
            TextView tvUserName;
            @BindView(R.id.tv_number)
            TextView tvNumber;
            @BindView(R.id.rl_root)
            RelativeLayout rlRoot;
            @BindView(R.id.iv_select)
            ImageView ivSelect;

            ContactViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
