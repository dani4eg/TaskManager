package model;

import java.io.Serializable;

/**
 * Created by darthMilash on 30.01.2017.
 */

public abstract class TaskList implements Iterable<Task>, Serializable {

    public  int size = 0;

    public  int size() {
        return this.size;
    }

    public abstract void add(Task task);

    public abstract Task getTask(int index);

    public abstract boolean remove (Task task);

    public String reInterval(int interval) {
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
            if (txt.equals("")) txt+= hours;
            else  txt+= " " + hours;
            if (hours>1) txt+= " hours";
            else txt+= " hour";
        }
        if (minutes>=1) {
            if (txt.equals("")) txt+= minutes;
            else  txt+= " " + minutes;
            if (minutes>1) txt+= " minutes";
            else txt+= " minute";
        }
        if (seconds>=1) {
            if (txt.equals("")) txt+= seconds;
            else  txt+= " " + seconds;
            if (seconds>1) txt+= " seconds";
            else txt+= " second";
        }
        return txt;
    }
}

