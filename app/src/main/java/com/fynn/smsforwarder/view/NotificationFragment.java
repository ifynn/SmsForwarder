package com.fynn.smsforwarder.view;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BaseFragment;
import com.fynn.smsforwarder.base.BasePresenter;
import com.fynn.smsforwarder.base.Model;
import com.fynn.smsforwarder.common.db.SPs;
import com.fynn.smsforwarder.model.consts.Consts;
import com.fynn.switcher.Switch;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.fynn.appu.util.CharsUtils;
import org.fynn.appu.util.ToastUtils;

/**
 * @author fynn
 */
public class NotificationFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvServerHost;
    private TextView tvServerPort;
    private TextView tvUsername;
    private TextView tvPassword;
    private Switch switchSSL;
    private TextView tvToAddress;

    private DataHandler handler;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvServerHost = (TextView) findViewById(R.id.tv_smpt_address);
        tvServerPort = (TextView) findViewById(R.id.tv_smpt_port);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvPassword = (TextView) findViewById(R.id.tv_password);
        switchSSL = (Switch) findViewById(R.id.switch_ssl_enabled);
        tvToAddress = (TextView) findViewById(R.id.tv_to_address);

        handler = (DataHandler) createModel();
    }

    @Override
    protected void initActions(Bundle savedInstanceState) {
        refresh();

        tvServerHost.setOnClickListener(this);
        tvServerPort.setOnClickListener(this);
        tvUsername.setOnClickListener(this);
        tvPassword.setOnClickListener(this);
        tvToAddress.setOnClickListener(this);

        switchSSL.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch s, boolean isChecked) {
                handler.save(R.id.switch_ssl_enabled, String.valueOf(isChecked));
            }
        });
    }

    private void refresh() {
        if (!CharsUtils.isEmptyAfterTrimming(handler.getSMTPAddress())) {
            tvServerHost.setText(handler.getSMTPAddress());
        }

        if (!CharsUtils.isEmptyAfterTrimming(handler.getSMTPPort())) {
            tvServerPort.setText(handler.getSMTPPort());
        }

        if (!CharsUtils.isEmptyAfterTrimming(handler.getUsername())) {
            tvUsername.setText(handler.getUsername());
        }

        if (!CharsUtils.isEmptyAfterTrimming(handler.getPassword())) {
            tvPassword.setText(handler.getPassword());
        }

        if (!CharsUtils.isEmptyAfterTrimming(handler.getToAddress())) {
            tvToAddress.setText(handler.getToAddress());
        }

        switchSSL.setChecked(handler.sslEnabled());
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected Model createModel() {
        return new DataHandler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_password:
            case R.id.tv_smpt_address:
            case R.id.tv_smpt_port:
            case R.id.tv_username:
            case R.id.tv_to_address:
                showEditTextDialog(v.getId());
                break;

            default:
                break;
        }
    }

    class DataHandler implements Model {

        public String getSMTPAddress() {
            return SPs.getSharedPreferences().getString(Consts.EmailConst.SERVER_HOST, "");
        }

        public String getSMTPPort() {
            return SPs.getSharedPreferences().getString(Consts.EmailConst.SERVER_PORT, "");
        }

        public String getToAddress() {
            return SPs.getSharedPreferences().getString(Consts.EmailConst.EMAIL, "");
        }

        public String getPassword() {
            return SPs.getSharedPreferences().getString(Consts.EmailConst.PASSWORD, "");
        }

        public String getUsername() {
            return SPs.getSharedPreferences().getString(Consts.EmailConst.USERNAME, "");
        }

        public boolean sslEnabled() {
            return SPs.getSharedPreferences().getBoolean(Consts.EmailConst.SSL, true);
        }

        public void save(int id, String text) {
            switch (id) {
                case R.id.tv_smpt_address:
                    SPs.getEditor().putString(Consts.EmailConst.SERVER_HOST, text).apply();
                    break;

                case R.id.tv_smpt_port:
                    SPs.getEditor().putString(Consts.EmailConst.SERVER_PORT, text).apply();
                    break;

                case R.id.tv_username:
                    SPs.getEditor().putString(Consts.EmailConst.USERNAME, text).apply();
                    break;

                case R.id.tv_password:
                    SPs.getEditor().putString(Consts.EmailConst.PASSWORD, text).apply();
                    break;

                case R.id.tv_to_address:
                    SPs.getEditor().putString(Consts.EmailConst.EMAIL, text).apply();
                    break;

                case R.id.switch_ssl_enabled:
                    SPs.getEditor().putBoolean(Consts.EmailConst.SSL, Boolean.valueOf(text)).apply();
                    break;

                default:
                    break;
            }
        }

    }

    private void showEditTextDialog(final int id) {
        String title = "";
        String tips = "";

        switch (id) {
            case R.id.tv_smpt_address:
                title = "SMTP 服务器地址设置";
                tips = " SMTP 服务器地址";
                break;

            case R.id.tv_smpt_port:
                title = "SMTP 服务器端口号设置";
                tips = " SMTP 服务器端口号";
                break;

            case R.id.tv_username:
                title = "用户名设置";
                tips = "用户名";
                break;

            case R.id.tv_password:
                title = "密码设置";
                tips = "密码";
                break;

            case R.id.tv_to_address:
                title = "收件箱地址设置";
                tips = "收件箱地址";
                break;

            default:
                break;
        }


        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        final String finalTips = tips;
        builder.setTitle(title)
                .setPlaceholder("在此输入" + tips)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            dialog.dismiss();
                            handler.save(id, text.toString());
                            initActions(null);
                        } else {
                            ToastUtils.showShortToast("请输入" + finalTips);
                        }
                    }
                })
                .show();
    }
}
