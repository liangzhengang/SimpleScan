package com.oggylzg.simpleble.client.scan;

import android.bluetooth.BluetoothDevice;

/**
 * Created by lenovo on 2018/11/17.
 */

public interface OnScanListener {
    int SCAN_FAILED_BLUETOOTH_DISABLE = 5;
    int SCAN_FAILED_BLE_NOT_SUPPORT = 6;
    int SCAN_FAILED_LOCATION_PERMISSION_DISABLE = 7;
    int SCAN_FAILED_LOCATION_PERMISSION_DISABLE_FOREVER = 8;

    void onScanResult(int rssi, byte[] byteRecord, BluetoothDevice device);

    void onScanningBefore();

    void onFail(int code);
}
