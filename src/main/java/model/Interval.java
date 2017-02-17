package model;

/**
 * Created by darthMilash on 17.02.2017.
 */
public class Interval {

    public static String reInterval(int interval) {
        int days = interval / 1000 / 86400;
        int hours = interval / 1000 / 3600 % 24;
        int minutes = interval / 1000 / 60 % 60;
        int seconds = interval / 1000 % 60;
        String txt="";
        if (days>=1) {
            txt+= days;
            if (days>1) txt+= " days";
            else txt+= " day";
        }
        if (hours>=1) {
            if ("".equals(txt)) txt+= hours;
            else  txt+= " " + hours;
            if (hours>1) txt+= " hours";
            else txt+= " hour";
        }
        if (minutes>=1) {
            if ("".equals(txt)) txt+= minutes;
            else  txt+= " " + minutes;
            if (minutes>1) txt+= " minutes";
            else txt+= " minute";
        }
        if (seconds>=1) {
            if ("".equals(txt)) txt+= seconds;
            else  txt+= " " + seconds;
            if (seconds>1) txt+= " seconds";
            else txt+= " second";
        }
        return txt;
    }


}
