package com.fynn.smsforwarder.view;

import android.os.Bundle;
import android.widget.TextView;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BaseFragment;
import com.fynn.smsforwarder.base.BasePresenter;
import com.fynn.smsforwarder.base.Model;
import com.fynn.smsforwarder.common.db.SPs;
import com.fynn.smsforwarder.model.consts.Consts;
import com.fynn.switcher.Switch;

import org.fynn.appu.util.CharsUtils;

/**
 * @author fynn
 */
public class NotificationFragment extends BaseFragment {

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
    }
}
