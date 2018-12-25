package com.oggylzg.simpleble.client.scan;

import android.annotation.TargetApi;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;

import java.util.List;

/**
 * Created by lenovo on 2018/11/17.
 */

public class HighVersionScan extends BaseScan {
    private static HighVersionScan mHighVersionScan;
    protected ScanCallback mScanCallback;

    public HighVersionScan(IScan baseScan) {
        if (baseScan == null) {
            throw new NullPointerException("null");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void stopScan() {
        mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallback);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initScanCallback(final OnScanListener listener) {
        if (mScanCallback == null) {
            mScanCallback = new ScanCallback() {

                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    listener.onScanResult(result.getRssi(), result.getScanRecord().getBytes(), result.getDevice());
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                }

                @Override
                public void onScanFailed(int errorCode) {
                    listener.onFail(errorCode);
                }
            };
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void doScan(OnScanListener listener) {
        mBluetoothAdapter.getBluetoothLeScanner().startScan(mScanCallback);
    }
}
