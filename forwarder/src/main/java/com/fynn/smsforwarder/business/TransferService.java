package com.fynn.smsforwarder.business;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Pair;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.common.SmsManager;
import com.fynn.smsforwarder.common.ThreadPool;
import com.fynn.smsforwarder.common.db.Dbs;
import com.fynn.smsforwarder.model.bean.Email;
import com.fynn.smsforwarder.model.bean.InboxSms;
import com.fynn.smsforwarder.view.MainActivity;

import org.fynn.appu.util.CharsUtils;
import org.fynn.appu.util.DateHelper;

import java.util.Date;

/**
 * @author Fynn
 * @date 18/2/13
 */
public class TransferService extends Service {

    private ContentObserver observer;

    private Runnable transferRunnable = new Runnable() {

        @Override
        public void run() {
            InboxSms s = Dbs.fetchRecentOneInboxSms();

            if (s == null) {
                return;
            }

            // 未读且非已收件
            if (s.read || s.type != 1) {
                return;
            }

            Pair<String, String> code = AuthCodeCache.get().fetchCode(s);
            String senderName = SmsManager.fetchSmsSender(s.msg);

            String subject;

            if (!CharsUtils.isEmptyAfterTrimming(code.first) &&
                    !CharsUtils.isEmptyAfterTrimming(code.second)) {
                subject = code.second + " (" + code.first + ")【" + senderName + "】";
            } else {
                subject = s.msg + "【" + s.address + "】";
            }

            String content = "发件人：" + s.address + "<br>" +
                    "发送时间：" + DateHelper.formatDate(new Date(s.date)) + "<br>" +
                    "短信内容：" + s.msg;

            Email email = EmailTransfer.genEmailData(subject, content, s.address);

            if (email == null) {
                return;
            }

            try {
                EmailTransfer.send(email);
                Dbs.insertSms(s);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private BatteryChangeReceiver mBatteryReceiver;

    public static void start(Context context) {
        Intent service = new Intent(context, TransferService.class);
        context.startService(service);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();

        observer = new SmsContentObserver();
        SmsManager.registerContentObserver(observer);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);

        mBatteryReceiver = new BatteryChangeReceiver();
        registerReceiver(mBatteryReceiver, filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 前台 Service 保活机制
     */
    private void startForegroundService() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, options);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, null);
        // todo 图标设置 alpha
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setLargeIcon(icon);
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(getText(R.string.notice_status_bar_ticker));
        builder.setAutoCancel(false);
        builder.setContentText(getText(R.string.notice_status_bar_content_text));
        builder.setContentTitle(getText(R.string.notice_status_bar_content_title));

        Intent activity = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0x01,
                activity, PendingIntent.FLAG_UPDATE_CURRENT));

        Notification notification = builder.build();
        startForeground(0x01, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SmsManager.unregisterContentObserver(observer);
        observer = null;

        ThreadPool.getInstance().shutdown();
        stopForeground(true);

        unregisterReceiver(mBatteryReceiver);
        mBatteryReceiver = null;
    }

    class SmsContentObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public SmsContentObserver(Handler handler) {
            super(handler);
        }

        public SmsContentObserver() {
            this(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            ThreadPool.getInstance().execute(transferRunnable);
        }
    }
}
