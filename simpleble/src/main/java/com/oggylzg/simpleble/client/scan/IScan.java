package com.oggylzg.simpleble.client.scan;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.NonNull;

/**
 * Created by lenovo on 2018/11/18.
 */

public interface IScan {
    int DEFAULT_SCAN_TIME = 5 * 1000;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    void stopScan();

    void startScan(int second, @NonNull final OnScanListener listener);
}
