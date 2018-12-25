package com.oggylzg.simpleble.client.scan;

import android.Manifest;
import android.support.annotation.NonNull;

import com.oggylzg.simpleble.permission.PermissionConstants;
import com.oggylzg.simpleble.permission.PermissionUtils;

import java.util.List;

import static com.oggylzg.simpleble.client.scan.OnScanListener.SCAN_FAILED_LOCATION_PERMISSION_DISABLE;
import static com.oggylzg.simpleble.client.scan.OnScanListener.SCAN_FAILED_LOCATION_PERMISSION_DISABLE_FOREVER;

/**
 * Created by lenovo on 2018/11/17.
 */

abstract public class BaseScan implements IScan {

    public abstract void stopScan();

    @Override
    public void startScan(int second, @NonNull final OnScanListener listener) {

        initScanCallback(listener);
        boolean isGranted = PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (isGranted) {
            listener.onScanningBefore();
            doScan(listener);

        } else {
            PermissionUtils.permission(PermissionConstants.LOCATION).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    listener.onScanningBefore();
                    doScan(listener);
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                    //权限被禁止
                    if (!permissionsDeniedForever.isEmpty()) {
                        listener.onFail(SCAN_FAILED_LOCATION_PERMISSION_DISABLE_FOREVER);
                    } else {
                        listener.onFail(SCAN_FAILED_LOCATION_PERMISSION_DISABLE);
                    }
                }
            }).request();
        }
    }


    protected abstract void initScanCallback(OnScanListener listener);

    protected abstract void doScan(OnScanListener listener);


}
