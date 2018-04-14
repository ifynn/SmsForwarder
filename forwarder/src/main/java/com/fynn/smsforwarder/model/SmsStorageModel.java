package com.fynn.smsforwarder.model;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.Model;
import com.fynn.smsforwarder.common.db.SPs;
import com.fynn.smsforwarder.common.db.SmsDbHelper;

/**
 *
 * @author Fynn
 * @date 18/2/6
 */

public class SmsStorageModel implements Model {

    public long getDbRecordCount() {
        return SmsDbHelper.get().getCount();
    }

    public String getSMTPAddress() {
        return SPs.getServerHost();
    }

    public String getSMTPPort() {
        return SPs.getServerPort();
    }

    public String getToAddress() {
        return SPs.getEmail();
    }

    public String getPassword() {
        return SPs.getPassword();
    }

    public String getUsername() {
        return SPs.getUsername();
    }

    public boolean sslEnabled() {
        return SPs.isEnabledSSL();
    }

    public boolean notifyBattery() {
        return SPs.isBatteryNotify();
    }

    public void save(int id, String text) {
        switch (id) {
            case R.id.tv_smpt_address:
                SPs.saveServerHost(text);
                break;

            case R.id.tv_smpt_port:
                SPs.saveServerPort(text);
                break;

            case R.id.tv_username:
                SPs.saveUsername(text);
                break;

            case R.id.tv_password:
                SPs.savePassword(text);
                break;

            case R.id.tv_to_address:
                SPs.saveEmail(text);
                break;

            case R.id.switch_ssl_enabled:
                SPs.saveSSL(Boolean.valueOf(text));
                break;

            case R.id.switch_battery_notify:
                SPs.saveBatteryNotify(Boolean.valueOf(text));
                break;

            default:
                break;
        }
    }
}
