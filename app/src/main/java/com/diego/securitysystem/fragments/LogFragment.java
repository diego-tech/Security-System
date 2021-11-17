package com.diego.securitysystem.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.diego.securitysystem.R;
import com.diego.securitysystem.SecurityApi;
import com.diego.securitysystem.adapter.LogAdapter;
import com.diego.securitysystem.models.HistoryLog;
import com.diego.securitysystem.sorts.SortByAlertStatus;
import com.diego.securitysystem.sorts.SortByDate;
import com.diego.securitysystem.sorts.SortByOffStatus;
import com.diego.securitysystem.sorts.SortByOnStatus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogFragment extends Fragment {

    Retrofit retrofit;
    SecurityApi api;
    List<HistoryLog> historyLogs = new ArrayList<>();
    ListView listView;
    View view;

    LogAdapter adapter;
    ArrayAdapter<CharSequence> selectorAdapter;
    Spinner selector;
    ImageView syncImg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log, container, false);

        listView = view.findViewById(R.id.securityList);
        syncImg = view.findViewById(R.id.syncImage);

        selector = view.findViewById(R.id.selector);
        selectorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.select, android.R.layout.simple_spinner_dropdown_item);
        selector.setAdapter(selectorAdapter);
        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (adapter != null) {
                    sorts();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        retrofitInit();
        getHistoryLogRequest();

        return view;
    }

    /* Coge los datos del GET y los setea en el Adapter de la Lista*/
    private void getHistoryLogRequest() {
        listView.setVisibility(View.GONE);
        syncImg.setVisibility(View.VISIBLE);
        api.getHistoryLog().enqueue(new Callback<List<HistoryLog>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<HistoryLog>> call, Response<List<HistoryLog>> response) {
                assert response.body() != null;
                historyLogs.removeAll(historyLogs);
                for (HistoryLog historyLog : response.body()) {
                    historyLogs.add(historyLog);
                }
                listView.setVisibility(View.VISIBLE);
                syncImg.setVisibility(View.GONE);
                setHistoryLogList();
            }

            @Override
            public void onFailure(Call<List<HistoryLog>> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sorts() {
        switch (selector.getSelectedItem().toString()) {
            case "Ordenar por:":
                historyLogs.sort(new SortByDate());
                Log.d("Ordenar", "Orden");
                break;
            case "Fecha":
                historyLogs.sort(new SortByDate());
                Log.d("Ordenar", "Date");
                break;
            case "Encendido":
                historyLogs.sort(new SortByOnStatus());
                Log.d("Ordenar", "Status ON");
                break;
            case "Apagado":
                historyLogs.sort(new SortByOffStatus());
                Log.d("Ordenar", "Status Off");
                break;
            case "Alerta":
                historyLogs.sort(new SortByAlertStatus());
                Log.d("Ordenar", "Status Alert");
                break;
        }
    }

    void setHistoryLogList() {
        listView = view.findViewById(R.id.securityList);
        adapter = new LogAdapter(getActivity(), R.layout.log_item, historyLogs);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static LogFragment newInstance() {
        return new LogFragment();
    }

    public void retrofitInit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(SecurityApi.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(SecurityApi.class);
    }
}