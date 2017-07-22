package com.cs.phoneguardian.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 2017/7/21.
 */

public class DialogUtils {

    public interface OnButtonClickedListener{
        void OnConfirm();
        void OnCancel();
    }

    /**
     * 弹出确定对话框
     * @param context 上下文
     * @param msg 弹出信息
     * @param listener 当点击按钮时的回调
     */
    public static void showConfirmDialog(Context context, String msg, final OnButtonClickedListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.OnConfirm();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.OnCancel();
                    }
                })
                .create();
        dialog.show();
    }
}
