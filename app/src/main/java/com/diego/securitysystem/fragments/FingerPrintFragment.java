package com.diego.securitysystem.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diego.securitysystem.LoginActivity;
import com.diego.securitysystem.MainActivity;
import com.diego.securitysystem.R;

import java.util.concurrent.Executor;

public class FingerPrintFragment extends Fragment {

    View view;
    androidx.biometric.BiometricManager biometricManager;

    Button logintBtn;
    TextView msg_text;

    public static FingerPrintFragment newInstance() {
        return new FingerPrintFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_finger_print, container, false);

        logintBtn = view.findViewById(R.id.login_btn);
        msg_text = view.findViewById(R.id.txt_msg);

        biometricManager();
        biometricPrompt();
        return view;
    }


    public void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.biometric_error_none_enrolled_title);
        builder.setMessage(R.string.biometric_error_none_enrolled);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), R.string.not_use_app, Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void biometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(getContext());

        BiometricPrompt biometricPrompt = new androidx.biometric.BiometricPrompt(FingerPrintFragment.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getContext().getText(R.string.login))
                .setDescription(getContext().getText(R.string.login_access_text))
                .setNegativeButtonText(getContext().getText(R.string.cancel))
                .build();


        logintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricManager();
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    public void biometricManager() {
        biometricManager = androidx.biometric.BiometricManager.from(getActivity());
        switch (biometricManager.canAuthenticate()) {
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msg_text.setText(R.string.biometric_error_hw_unavailable);
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                dialog();
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msg_text.setText(R.string.biometric_error_no_hardware);
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS:
                msg_text.setText(R.string.biometric_success);
                biometricPrompt();
                break;
        }
    }

}