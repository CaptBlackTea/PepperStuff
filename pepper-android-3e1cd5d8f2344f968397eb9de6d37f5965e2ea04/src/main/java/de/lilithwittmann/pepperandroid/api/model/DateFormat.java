package de.lilithwittmann.pepperandroid.api.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yip on 10/10/2016.
 */

public class DateFormat {

    private static final String TODAY = "Today ";
//    private static final String TODAY = "Today";

    private static long today = System.currentTimeMillis() % 1000;

    public static String getDateTime(String timestamp){
        String date = getDate(Long.parseLong(timestamp));
        //todo make it contain "today" "yesterday" "tomorrow"
        Log.e( "getDateTime: ", date.substring(0, 10));
        Log.e( "today: ", getToday());
        if(date.substring(0, 10).equals(getToday().substring(0, 10))){
            date = TODAY+date.substring(11);
            Log.e( "getDateTime: ", date);
        }
        return date;
    }

    private static String getDate(long timestamp){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Date date = new Date(timestamp);
        return sf.format(date);
    }

    public static String getToday(){
        return getDate(System.currentTimeMillis());
    }


}
