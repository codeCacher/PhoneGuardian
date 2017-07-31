package com.cs.phoneguardian.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.phoneguardian.R;

/**
 * Created by Administrator on 2017/7/21.
 */

public class DialogUtils {

    public interface OnButtonClickedListener {
        void OnConfirm();

        void OnCancel();
    }

    /**
     * 弹出确定对话框
     *
     * @param context  上下文
     * @param msg      弹出信息
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

    /**
     * 显示输入密码对话框
     * @param context 上下文
     * @param correctPsd 正确的密码
     * @param listener 点击按钮之后的回调函数
     */
    public static void showPasswordDialog(final Context context, final String correctPsd, final OnButtonClickedListener listener) {
        View view = View.inflate(context, R.layout.password_dialog, null);
        final AlertDialog passwordDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view)
                .create();

        final EditText etPsd = (EditText) view.findViewById(R.id.et_psd);
        TextView tvOK = (TextView) view.findViewById(R.id.tv_ok);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);

        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPsd = etPsd.getText().toString();
                String MD5InputPsd = MD5Utils.MD5Encode(inputPsd);
                if (MD5InputPsd.equals(correctPsd)) {
                    listener.OnConfirm();
                    passwordDialog.dismiss();
                } else {
                    Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                    etPsd.setText("");
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordDialog.dismiss();
                listener.OnCancel();
            }
        });

        passwordDialog.show();
    }
}
