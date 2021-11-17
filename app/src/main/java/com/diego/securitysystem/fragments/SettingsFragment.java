package com.diego.securitysystem.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.diego.securitysystem.R;

public class SettingsFragment extends Fragment {

    View view;
    EditText ipHolder1, ipHolder2, ipHolder3, ipHolder4;
    Button configFingerPrint, configIp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        ipHolder1 = view.findViewById(R.id.ipHolder1);
        ipHolder2 = view.findViewById(R.id.ipHolder2);
        ipHolder3 = view.findViewById(R.id.ipHolder3);
        ipHolder4 = view.findViewById(R.id.ipHolder4);
        configFingerPrint = view.findViewById(R.id.configFingerprint);
        configIp = view.findViewById(R.id.configIp);

        configIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchIP();
            }
        });

        configFingerPrint();
        return view;
    }

    public void launchIP() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View ipDialog = layoutInflater.inflate(R.layout.ip_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Configurar IP");
        builder.setMessage("La IP no puede ser mayor de 255");
        builder.setView(ipDialog);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }


    public void configFingerPrint() {
        configFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
            }
        });
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
}