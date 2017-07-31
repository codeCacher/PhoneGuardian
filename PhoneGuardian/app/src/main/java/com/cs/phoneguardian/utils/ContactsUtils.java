package com.cs.phoneguardian.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.cs.phoneguardian.bean.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/16.
 * 获取手机联系人列表的工具类
 */

public class ContactsUtils {
    public static List<Contact> readContacts(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor = contentResolver.query(uri, new String[]{"contact_id"}, null, null, null);

        List<Contact> contactsList = new ArrayList<>();
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                if(id == null) {
                    continue;
                }
                Contact contact = new Contact();
                contact.setId(id);

                Uri contacturi = Uri.parse("content://com.android.contacts/data");
                Cursor contactCursor = contentResolver.query(contacturi, new String[]{"mimetype", "data1"}, "raw_contact_id=?", new String[]{id}, null);
                if(contactCursor != null && contactCursor.getCount() != 0) {
                    while (contactCursor.moveToNext()) {
                        String type = contactCursor.getString(0);
                        String data = contactCursor.getString(1);

                        switch (type) {
                            case "vnd.android.cursor.item/name":
                                contact.setName(data);
                                break;
                            case "vnd.android.cursor.item/phone_v2":
                                contact.setPhoneNumber(data);
                                break;
                            case "vnd.android.cursor.item/postal-address_v2":
                                contact.setAddress(data);
                                break;
                            case "vnd.android.cursor.item/email_v2":
                                contact.setEmail(data);
                                break;
                        }
                    }
                    contact.setSelected(false);
                    contactsList.add(contact);
                    contactCursor.close();
                }
            }
            cursor.close();
        }
            return contactsList;
    }
}
