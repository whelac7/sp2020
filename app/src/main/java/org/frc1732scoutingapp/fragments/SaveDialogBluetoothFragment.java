package org.frc1732scoutingapp.fragments;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.models.RequestCodes;

import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class SaveDialogBluetoothFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_dialog_bluetooth, container, false);
        Button syncButton = view.findViewById(R.id.syncBluetoothButton);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            if (device.getName().toLowerCase().equals(getResources().getString(R.string.bluetooth_master_device_name).toLowerCase())) {
                                Toast.makeText(getActivity(), "Found existing master tablet.", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                    }

                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    Intent rec = getActivity().registerReceiver(receiver, filter);
                    System.out.println(rec);
                    System.out.println(bluetoothAdapter.startDiscovery());
                };
            }
        });
        return view;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Toast.makeText(getActivity(), "Found Device", Toast.LENGTH_LONG).show();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                Log.d("deviceName", deviceName);
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
        getActivity().unregisterReceiver(receiver);
    }
}
