package com.nsoft.sqliteapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFunctionClasses {

    public String timeStampToFormattedDateTime(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");

        return dateFormat.format(date);
    }
}
