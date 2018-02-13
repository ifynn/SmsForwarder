package com.fynn.smsforwarder.business;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.common.SmsManager;
import com.fynn.smsforwarder.common.ThreadPool;
import com.fynn.smsforwarder.common.db.Dbs;
import com.fynn.smsforwarder.model.bean.Sms;
import com.fynn.smsforwarder.view.MainActivity;

/**
 * @author Fynn
 * @date 18/2/13
 */
public class TransferService extends Service {

    private ContentObserver observer;

    private Runnable transferRunnable = new Runnable() {

        @Override
        public void run() {
            Sms s = Dbs.fetchRecentOneInboxSms();

            if (s == null) {
                return;
            }


        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();

        observer = new SmsContentObserver();
        SmsManager.registerContentObserver(observer);
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
        builder.setSmallIcon(R.mipmap.ic_launcher);
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
    }
}
