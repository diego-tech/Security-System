package com.diego.securitysystem.sorts;

import com.diego.securitysystem.models.HistoryLog;

import java.util.Comparator;

public class SortByDate implements Comparator<HistoryLog> {
    @Override
    public int compare(HistoryLog o1, HistoryLog o2) {
        if (o1.getDate() < o2.getDate()) return -1;
        else if (o1.getDate() == o2.getDate()) return 0;
        else return 1;
    }
}
