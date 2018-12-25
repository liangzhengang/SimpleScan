package com.oggylzg.simpleble.client.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

/**
 * Created by lenovo on 2018/11/18.
 */

public class LowVersionScan extends BaseScan {
    protected BluetoothAdapter.LeScanCallback mLeScanCallback;

    public LowVersionScan(IScan baseScan) {
        if (baseScan == null) {
            throw new NullPointerException("null");
        }
    }

    @Override
    public void stopScan() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    @Override
    public void initScanCallback(final OnScanListener listener) {
        if (mLeScanCallback == null) {
            mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
                    listener.onScanResult(i, bytes, bluetoothDevice);
                }

            };
        }
    }

    @Override
    public void doScan(OnScanListener listener) {
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }
}
