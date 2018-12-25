package com.oggylzg.simpleble.client.scan;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2018/11/18.
 */

public class ScanProxy implements IScan {
    private static final int MsgWhat_stopScan = 0;
    private static final int MsgWhat_startScan = 1;
    static Handler scanSleepHandler;
    private static IScan mScan = null;
    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    int sleepSecond, durationSecond = DEFAULT_SCAN_TIME;
    Runnable runnable;
    Context context;
    boolean isScanning;
    OnScanListener listener;
    Handler sleepHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MsgWhat_startScan) {
                mScan.stopScan();
                mScan.startScan(sleepSecond, listener);
                sleepHandler.removeMessages(MsgWhat_startScan);
                sleepHandler.sendEmptyMessageDelayed(MsgWhat_stopScan, durationSecond);
            } else {
                mScan.stopScan();
                sleepHandler.removeMessages(MsgWhat_stopScan);
                sleepHandler.sendEmptyMessageDelayed(MsgWhat_startScan, sleepSecond);
            }
        }
    };

    private ScanProxy(Context context) {
        this.context = context;
    }

    public static ScanProxy getInstance(Context context) {
        ScanProxy scanProxy = new ScanProxy(context);
        if (mScan == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mScan = new HighVersionScan(scanProxy);
            } else {
                mScan = new LowVersionScan(scanProxy);
            }
        }
        return scanProxy;
    }

    @Override
    public void stopScan() {
        sleepHandler.removeMessages(MsgWhat_startScan);
        sleepHandler.removeMessages(MsgWhat_stopScan);
        mScan.stopScan();
    }

    @Override
    public void startScan(final int second, @NonNull final OnScanListener listener) {
        if (!isSupportBle(context)) {
            listener.onFail(OnScanListener.SCAN_FAILED_BLE_NOT_SUPPORT);
            return;
        }
        if (!isBlueEnabled()) {
            listener.onFail(OnScanListener.SCAN_FAILED_BLUETOOTH_DISABLE);
            return;
        }
        this.sleepSecond = second;
        this.listener = listener;
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    stopScan();
                    mScan.startScan(second, listener);
                    if (sleepSecond > 0) {
                        sleepHandler.removeMessages(MsgWhat_startScan);
                        sleepHandler.removeMessages(MsgWhat_stopScan);
                        sleepHandler.sendEmptyMessageDelayed(MsgWhat_stopScan, durationSecond);
                    }
                }
            };
        }
        singleThreadExecutor.submit(runnable);
    }

    public static boolean isSupportBle(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return false;
        }
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        return true;
    }

    public static boolean isBlueEnabled() {
        return mBluetoothAdapter.isEnabled();
    }
}
