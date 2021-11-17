package com.diego.securitysystem.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diego.securitysystem.R;
import com.diego.securitysystem.models.HistoryLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LogAdapter extends ArrayAdapter {

    Context context;
    int itemLayout;
    List<HistoryLog> historyLogs;
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMMM dd hh:mm aaa");

    public LogAdapter(@NonNull Context context, int itemLayout, List<HistoryLog> historyLogs) {
        super(context, itemLayout);
        this.context = context;
        this.itemLayout = itemLayout;
        this.historyLogs = historyLogs;
    }

    @Override
    public int getCount() {
        return historyLogs.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(itemLayout, parent, false);
        }

        String status = historyLogs.get(position).getEvent();
        Long date = historyLogs.get(position).getDate();
        String dateTime = dateTimeFormat.format(new Date(date));

        TextView statusTV = convertView.findViewById(R.id.status);
        TextView dateTV = convertView.findViewById(R.id.date);
        ImageView iconImage = convertView.findViewById(R.id.iconLog);

        statusTV.setText(status);
        dateTV.setText(dateTime);

        switch (historyLogs.get(position).getEvent()) {
            case ("on"):
                statusTV.setText("Sistema Encendido");
                iconImage.setImageResource(R.drawable.ic_checked);
                statusTV.setTextColor(context.getResources().getColor(R.color.black));
                statusTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                statusTV.setTypeface(Typeface.DEFAULT);
                break;
            case ("off"):
                statusTV.setText("Sistema Apagado");
                iconImage.setImageResource(R.drawable.ic_uncheck);
                statusTV.setTextColor(context.getResources().getColor(R.color.black));
                statusTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                statusTV.setTypeface(Typeface.DEFAULT);
                break;
            case ("alert"):
                statusTV.setText("Alerta Intruso!!");
                iconImage.setImageResource(R.drawable.ic_alert);
                statusTV.setTextColor(context.getResources().getColor(R.color.red));
                statusTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25.f);
                statusTV.setTypeface(Typeface.DEFAULT_BOLD);
                break;
        }

        return convertView;
    }
}
