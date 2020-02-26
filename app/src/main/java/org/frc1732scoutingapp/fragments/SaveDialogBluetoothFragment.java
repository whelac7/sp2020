package org.frc1732scoutingapp.fragments;

import android.app.AlertDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.models.RequestCodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class SaveDialogBluetoothFragment extends Fragment {
    private Intent bluetoothReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_dialog_bluetooth, container, false);
        Button syncButton = view.findViewById(R.id.syncBluetoothButton);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                bluetoothReceiver = getActivity().registerReceiver(receiver, filter);
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    Toast.makeText(getActivity(), "This device does not support bluetooth.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, RequestCodes.REQUEST_ENABLE_BT.getValue());
                }
                else {
                    BluetoothDevice device = isConnectedToMaster(bluetoothAdapter);
//                    new AlertDialog.Builder(getContext())
//                            .setTitle("You are not paired with the master tablet!")
//                            .setMessage("Please pair with the master tablet and name it " + "\"" + getResources().getString(R.string.bluetooth_master_device_name) + ".\"")
//                            .setPositiveButton(android.R.string.ok, (dialog, which) -> openBluetoothSettings())
//                            .setNegativeButton(android.R.string.cancel, null)
//                            .show();
                }
            }
        });
        return view;
    }

    private BluetoothDevice isConnectedToMaster(BluetoothAdapter bluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (getAliasName(device).toLowerCase().equals(getResources().getString(R.string.bluetooth_master_device_name).toLowerCase())) {
                    return device;
                }
            }
        }
        return null;
//        BluetoothProfile.ServiceListener profileListener = new BluetoothProfile.ServiceListener() {
//            @Override
//            public void onServiceConnected(int profile, BluetoothProfile proxy) {
//                if (profile == BluetoothProfile.A2DP) {
//                    boolean isConnected = false;
//                    BluetoothA2dp btA2dp = (BluetoothA2dp)proxy;
//                    List<BluetoothDevice> devices = btA2dp.getConnectedDevices();
//                    if (devices.size() > 0) {
//                        for (BluetoothDevice device : devices) {
//                            if (getAliasName(device).toLowerCase().equals(getResources().getString(R.string.bluetooth_master_device_name).toLowerCase())) {
//                                isConnected = true;
//                            }
//                        }
//                        bluetoothAdapter.closeProfileProxy(profile, proxy);
//                    }
//                }
//            }
//
//            @Override
//            public void onServiceDisconnected(int profile) {
//                // TODO
//            }
//        };
//        return bluetoothAdapter.getProfileProxy(getContext(), profileListener, BluetoothProfile.A2DP);
    }

    private void openBluetoothSettings() {
        Intent openBluetoothSettingsIntent = new Intent();
        openBluetoothSettingsIntent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(openBluetoothSettingsIntent);
    }

    private String getAliasName(BluetoothDevice device) {
        String deviceAlias = device.getName();
        try {
            Method method = device.getClass().getMethod("getAliasName");
            if(method != null) {
                deviceAlias = (String)method.invoke(device);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return deviceAlias;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                if (deviceName != null) {
                    System.out.println(deviceName);
                }
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {

            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.REQUEST_ENABLE_BT.getValue() && resultCode == RESULT_OK) {
            Toast.makeText(getActivity(), "Connected to Bluetooth!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bluetoothReceiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }
}
