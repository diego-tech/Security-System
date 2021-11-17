package com.diego.securitysystem.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.diego.securitysystem.R;
import com.diego.securitysystem.SecurityApi;
import com.diego.securitysystem.models.State;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class StatusFragment extends Fragment {

    Retrofit retrofit;
    SecurityApi api;
    View view;

    ImageView statusImgInfo;
    TextView statusText;

    ImageButton onButton, offButton;
    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_status, container, false);

        onButton = view.findViewById(R.id.button);
        offButton = view.findViewById(R.id.button1);
        statusImgInfo = view.findViewById(R.id.statusImgInfo);
        frameLayout = view.findViewById(R.id.frameLayout);
        statusText = view.findViewById(R.id.statusText);

        retrofiInit();
        changeStatus();
        getState();
        return view;
    }

    public void changeOnStatus() {
        Executor executor = ContextCompat.getMainExecutor(getContext());

        BiometricPrompt biometricPrompt = new BiometricPrompt(StatusFragment.this, executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.d("Error", "Error Autenticación");
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                api.putState(new State("on")).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        getState();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
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

        biometricPrompt.authenticate(promptInfo);
    }

    public void changeOffStatus() {
        Executor executor = ContextCompat.getMainExecutor(getContext());

        BiometricPrompt biometricPrompt = new BiometricPrompt(StatusFragment.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                api.putState(new State("off")).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        getState();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        getState();
                    }
                });
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

        biometricPrompt.authenticate(promptInfo);
    }

    public void changeStatus() {
        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOnStatus();
            }
        });

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOffStatus();
            }
        });
    }

    public void getState() {
        statusImgInfo.setImageResource(R.drawable.ic_sync);
        statusText.setText(R.string.loadText);
        onButton.setClickable(false);
        onButton.setAlpha(0.1f);
        offButton.setClickable(false);
        offButton.setAlpha(0.1f);
        api.getState().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String status = response.body();
                assert status != null;
                switchImg(status);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void switchImg(String status) {
        switch (status) {
            case "on":
                statusImgInfo.setImageResource(R.drawable.armed);
                statusText.setText(R.string.onText);
                onButton.setClickable(false);
                onButton.setAlpha(0.1f);
                offButton.setClickable(true);
                offButton.setAlpha(1f);
                frameLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case "off":
                statusImgInfo.setImageResource(R.drawable.disarmed);
                statusText.setText(R.string.offText);
                onButton.setClickable(true);
                onButton.setAlpha(1f);
                offButton.setClickable(false);
                offButton.setAlpha(0.1f);
                frameLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case "alert":
                onButton.setClickable(true);
                offButton.setClickable(true);
                onButton.setAlpha(1f);
                offButton.setAlpha(1f);
                statusImgInfo.setImageResource(R.drawable.ic_alert);
                statusText.setText(R.string.alertText);
                frameLayout.setBackgroundColor(Color.parseColor("#E50027"));
                break;
        }
    }

    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    public void retrofiInit() {

        retrofit = new Retrofit.Builder()
                .baseUrl(SecurityApi.SERVER_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(SecurityApi.class);
    }
}