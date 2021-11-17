package com.diego.securitysystem.sorts;

import com.diego.securitysystem.models.HistoryLog;

import java.util.Comparator;

public class SortByOnStatus implements Comparator<HistoryLog> {
    @Override
    public int compare(HistoryLog o1, HistoryLog o2) {
        if (o1.getEvent().equals("on")) {
            return -1;
        } else return 1;
    }
}
