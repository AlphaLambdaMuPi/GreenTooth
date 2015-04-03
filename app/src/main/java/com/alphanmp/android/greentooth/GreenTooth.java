package com.alphanmp.android.greentooth;

import android.app.admin.DeviceAdminInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;


public class GreenTooth extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_tooth);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_green_tooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBLEScan;
    private boolean mScanning = false;
    private BluetoothDevice mDev;
    private BluetoothGatt mBluetoothGatt;

    // Device scan callback.
    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Log.d("Debug", "Alpha " + device.toString());

            mDev = device;
            Log.d("Scan", "Stop");
            mBLEScan.stopScan(mLeScanCallback);
            mScanning = false;
            searched();
        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.d("Debug", "Beta");
        }
        @Override
        public void onScanFailed(int errorCode) {
            Log.d("Debug", "QAQ " + errorCode);
        }
    };

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.w("Alpha", "onServicesDiscovered received: " + status);
            } else {
                Log.w("QAQ", "onServicesDiscovered received: " + status);
            }
        }
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.d("RSSI", "" + rssi);
        }
        @Override
        public void onConnectionStateChange (BluetoothGatt gatt, int status, int newState) {
            Log.d("State", status + " " + newState);
            gatt.discoverServices();
            boolean ret = gatt.readRemoteRssi();
            Log.d("Zeta", ret + " " + gatt.getServices().toString());
        }
    };

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private void scanLeDevice(final boolean enable) {
        if(!mScanning) {
            Log.d("Scan", "Start");
            mBLEScan.startScan(mLeScanCallback);
            mScanning = true;
        }
        else {
            Log.d("Scan", "Stop");
            mBLEScan.stopScan(mLeScanCallback);
            mScanning = false;
        }
    }

    private void init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBLEScan = mBluetoothAdapter.getBluetoothLeScanner();
    }


    public void alpha(View view) {
        scanLeDevice(true);
    }

    public void searched() {
        mBluetoothGatt = mDev.connectGatt(this, true, mGattCallback);
        //mBluetoothGatt.connect();
        //mBluetoothGatt.discoverServices();
        //boolean ret = mBluetoothGatt.readRemoteRssi();
        //Log.d("Alpha", ret + " " + mBluetoothGatt.getServices().toString());

    }
}

