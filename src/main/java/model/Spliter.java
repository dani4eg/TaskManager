package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by darthMilash on 17.02.2017.
 */
public  class Spliter {

    public static Task splitString(String str) throws ParseException {
        Task task;
        String title;
        Date dStart = null;
        Date dEnd = null;
        Date dTime = null;
        int interval;
        boolean active = false;

        title = reTitle(str);
        dStart = reStart(str);
        dEnd = reEnd(str);
        dTime = reTime(str);
        interval = reInterval(str);
        active = reActive(str);

        if (dStart==null && dEnd==null) {
            task = new Task(title, dTime);
        }
        else task = new Task(title, dStart, dEnd, interval);
        if (active) {
            task.setActive(true);
        }
        return task;
    }

    private static String reTitle(String str) {
        String title = "";
        Pattern patTitle = Pattern.compile("\".+\"");
        Matcher mTitle = patTitle.matcher(str);
        if (mTitle.find()) {
            title = str.substring(mTitle.start()+1, mTitle.end()-1);
        }
        return title;
    }

    private static Date reStart(String str) throws ParseException {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS", Locale.ENGLISH);
        Date dStart = new Date();
        String start;
        Pattern patStart = Pattern.compile("from.+to");
        Matcher mStart = patStart.matcher(str);
        if (mStart.find()) {
            start = str.substring(mStart.start()+6, mStart.end()-4);
            dStart=date.parse(start);
        }
        return dStart;
    }

    private static Date reEnd(String str) throws ParseException {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS", Locale.ENGLISH);
        Date dEnd = new Date();
        String end = "";
        Pattern patEnd = Pattern.compile("to.+every");
        Matcher mEnd = patEnd.matcher(str);
        if (mEnd.find()) {
            end = str.substring(mEnd.start()+4, mEnd.end()-7);
            dEnd=date.parse(end);
        }
        return dEnd;
    }

    private static Date reTime(String str) throws ParseException {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS", Locale.ENGLISH);
        Date dTime = new Date();
        String time = "";
        Pattern patTime = Pattern.compile("at.+]");
        Matcher mTime = patTime.matcher(str);
        if (mTime.find()) {
            time = str.substring(mTime.start()+4, mTime.end()-1);
            dTime = date.parse(time);
        }
        return dTime;
    }

    private static boolean reActive(String str) {
        boolean active = false;
        Pattern patActive = Pattern.compile("inactive");
        Matcher mActive = patActive.matcher(str);
        if (mActive.find()) {
            active = true;
        }
        return active;
    }

    private static int reInterval(String str) {
        int interval = 0;
        String days = "";
        String hours = "";
        String minutes = "";
        String seconds = "";
        Pattern pat = Pattern.compile("every.+day");
        Matcher matcher = pat.matcher(str);
        if (matcher.find()) {
            days = str.substring(matcher.start()+7, matcher.end()-4);
            interval=Integer.parseInt(days) * 86400;
        }
        Pattern pat2 = Pattern.compile("hour");
        Matcher matcher2 = pat2.matcher(str);
        if (matcher2.find()) {
            hours = str.substring(matcher2.start()-3, matcher2.end()-5);
            try {
                interval+= Integer.parseInt(hours) * 3600;
            }
            catch (NumberFormatException e) {
                String cHours = str.substring(matcher2.start()-2, matcher2.end()-5);
                interval+= Integer.parseInt(cHours) * 3600;
            }
        }
        Pattern pat3 = Pattern.compile("minute");
        Matcher matcher3 = pat3.matcher(str);
        if (matcher3.find()) {
            minutes = str.substring(matcher3.start()-3, matcher3.end()-7);
            try {
                interval+= Integer.parseInt(minutes) * 60;
            }
            catch (NumberFormatException e) {
                String cMinutes = str.substring(matcher3.start()-2, matcher3.end()-7);
                interval+= Integer.parseInt(cMinutes) * 60;
            }
        }
        Pattern patSec = Pattern.compile("second");
        Matcher mSec = patSec.matcher(str);
        if (mSec.find()) {
            seconds = str.substring(mSec.start()-3, mSec.end()-7);
            try {
                interval+= Integer.parseInt(seconds);
            }
            catch (NumberFormatException e) {
                String cSeconds = str.substring(mSec.start()-2, mSec.end()-7);
                interval+= Integer.parseInt(cSeconds);
            }
        }
        return interval;
    }
}
