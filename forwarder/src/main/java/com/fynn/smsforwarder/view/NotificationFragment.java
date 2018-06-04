package com.fynn.smsforwarder.view;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BaseFragment;
import com.fynn.smsforwarder.business.DefaultPresenter;
import com.fynn.smsforwarder.model.SmsStorageModel;
import com.fynn.switcher.Switch;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.fynn.appu.util.ToastUtils;

/**
 * @author fynn
 */
public class NotificationFragment extends BaseFragment<BaseView, SmsStorageModel, DefaultPresenter>
        implements View.OnClickListener, BaseView {

    private TextView tvServerHost;
    private TextView tvServerPort;
    private TextView tvUsername;
    private TextView tvPassword;
    private Switch switchSSL;
    private TextView tvToAddress;
    private Switch switchBatteryNotify;

    private ViewInteraction interaction;
    private boolean hided;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(ViewInteraction interaction) {
        NotificationFragment fragment = new NotificationFragment();
        fragment.interaction = interaction;
        fragment.setRetainInstance(true);
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
        switchBatteryNotify = (Switch) findViewById(R.id.switch_battery_notify);
    }

    @Override
    protected void initActions(Bundle savedInstanceState) {
        mPresenter.updateNotificationView();

        tvServerHost.setOnClickListener(this);
        tvServerPort.setOnClickListener(this);
        tvUsername.setOnClickListener(this);
        tvPassword.setOnClickListener(this);
        tvToAddress.setOnClickListener(this);

        switchSSL.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch s, boolean isChecked) {
                mPresenter.save(R.id.switch_ssl_enabled, String.valueOf(isChecked));
            }
        });

        switchBatteryNotify.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch s, boolean isChecked) {
                mPresenter.save(R.id.switch_battery_notify, String.valueOf(isChecked));
            }
        });

        tvPassword.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                hided = !hided;

                if (hided) {
                    tvPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    tvPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }

                return true;
            }
        });
    }

    @Override
    protected DefaultPresenter createPresenter() {
        return new DefaultPresenter();
    }

    @Override
    protected SmsStorageModel createModel() {
        return interaction.getModel();
    }

    public void updateText(int vid, String text) {
        switch (vid) {
            case R.id.tv_password:
                tvPassword.setText(text);
                if (hided) {
                    tvPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    tvPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;

            case R.id.tv_smpt_address:
                tvServerHost.setText(text);
                break;

            case R.id.tv_smpt_port:
                tvServerPort.setText(text);
                break;

            case R.id.tv_username:
                tvUsername.setText(text);
                break;

            case R.id.tv_to_address:
                tvToAddress.setText(text);
                break;

            default:
                break;
        }
    }

    public void updateChecked(int vid, boolean checked) {
        switch (vid) {
            case R.id.switch_battery_notify:
                switchBatteryNotify.setChecked(checked);
                break;

            case R.id.switch_ssl_enabled:
                switchSSL.setChecked(checked);
                break;

            default:
                break;
        }
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
                            if (id == R.id.tv_password) {
                                hided = false;
                            }

                            mPresenter.save(id, text.toString());
                            mPresenter.updateNotificationView();

                        } else {
                            ToastUtils.showShortToast("请输入" + finalTips);
                        }
                    }
                })
                .show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser) {
            hided = true;
        }
    }
}
