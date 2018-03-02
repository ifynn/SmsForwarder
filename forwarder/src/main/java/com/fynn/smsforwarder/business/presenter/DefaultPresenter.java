package com.fynn.smsforwarder.business.presenter;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BasePresenter;
import com.fynn.smsforwarder.common.db.Dbs;
import com.fynn.smsforwarder.model.SmsStorageModel;
import com.fynn.smsforwarder.view.BaseView;
import com.fynn.smsforwarder.view.NotificationFragment;

import org.fynn.appu.util.CharsUtils;

import java.util.List;

/**
 * @author Fynn
 * @date 18/3/1
 */
public class DefaultPresenter extends BasePresenter<BaseView, SmsStorageModel> {

    public long getDbRecordCount() {
        return mModel.getDbRecordCount();
    }

    public void updateNotificationView() {
        if (!(getView() instanceof NotificationFragment)) {
            return;
        }

        NotificationFragment f = (NotificationFragment) getView();
        String tmp = mModel.getSMTPAddress();

        if (!CharsUtils.isEmptyAfterTrimming(tmp)) {
            f.updateText(R.id.tv_smpt_address, tmp);
        }

        tmp = mModel.getSMTPPort();

        if (!CharsUtils.isEmptyAfterTrimming(tmp)) {
            f.updateText(R.id.tv_smpt_port, tmp);
        }

        tmp = mModel.getUsername();

        if (!CharsUtils.isEmptyAfterTrimming(tmp)) {
            f.updateText(R.id.tv_username, tmp);
        }

        tmp = mModel.getPassword();

        if (!CharsUtils.isEmptyAfterTrimming(tmp)) {
            f.updateText(R.id.tv_password, tmp);
        }

        tmp = mModel.getToAddress();

        if (!CharsUtils.isEmptyAfterTrimming(tmp)) {
            f.updateText(R.id.tv_to_address, tmp);
        }

        boolean checked = mModel.notifyBattery();
        f.updateChecked(R.id.switch_battery_notify, checked);

        checked = mModel.sslEnabled();
        f.updateChecked(R.id.switch_ssl_enabled, checked);
    }

    public void save(int id, String text) {
        mModel.save(id, text);
    }

    public List readSms(int offsetStart, int pageCount) {
        return Dbs.readSmsPage(offsetStart, pageCount);
    }
}
