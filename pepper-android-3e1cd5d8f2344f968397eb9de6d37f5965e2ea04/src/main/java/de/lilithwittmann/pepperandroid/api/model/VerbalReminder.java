package de.lilithwittmann.pepperandroid.api.model;

/**
 * Created by Yip on 7/10/2016.
 */

public class VerbalReminder {

    private static final String SPACE = " ";
    private String timeHeader = "You have a message at ";
    private String time;
    private String content;
    private String reminder;

    public VerbalReminder(String time, String content) {
        this.time = time;
        this.content = content;
        reminder = timeHeader+timeToDate()+content;
    }

    public String getReminder() {
        return reminder;
    }

    private String timeToDate(){
        return DateFormat.getDateTime(time);
    }

}
