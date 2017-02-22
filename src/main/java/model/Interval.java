package model;

/**
 * Created by darthMilash on 17.02.2017.
 */
public class Interval {

    private static String text;
    public static String reInterval(int interval) {
        text = "";
        int days = interval / 1000 / 86400;
        int hours = interval / 1000 / 3600 % 24;
        int minutes = interval / 1000 / 60 % 60;
        int seconds = interval / 1000 % 60;
        if (days>=1) {
            text+=days(days);
        }
        if (hours>=1) {
            text+=hours(hours);
        }
        if (minutes>=1) {
            text+=minutes(minutes);
        }
        if (seconds>=1) {
            text+=seconds(seconds);
        }
        return text;
    }

    private static String days(int days) {
        String txt = "";
        txt+= days;
        if (days>1) txt+= " days";
        else txt+= " day";
        return txt;
    }
    private static String hours(int hours) {
        String txt = "";
        if ("".equals(text)) txt+= hours;
        else  txt+= " " + hours;
        if (hours>1) txt+= " hours";
        else txt+= " hour";
        return txt;
    }
    private static String minutes(int minutes) {
        String txt = "";
        if ("".equals(text)) txt+= minutes;
        else  txt+= " " + minutes;
        if (minutes>1) txt+= " minutes";
        else txt+= " minute";
        return txt;
    }
    private static String seconds(int seconds) {
        String txt = "";
        if ("".equals(text)) txt+= seconds;
        else  txt+= " " + seconds;
        if (seconds>1) txt+= " seconds";
        else txt+= " second";
        return txt;
    }

}
