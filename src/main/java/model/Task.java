package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by darthMilash on 30.01.2017.
 */
public class Task implements Cloneable, Serializable {

    private String title;
    private Date start;
    private Date end;
    private int interval;
    private boolean active;

    /**
     * Constructor for task without interval
     * @param title - name of task
     * @param time - date of task
     * @throws IllegalArgumentException
     */
    public Task(String title, int time) throws IllegalArgumentException {
        this.title = title;
        this.start = new Date(time*1000);
        this.end = new Date(time *1000);
        if (time < 0 ) {
            throw new IllegalArgumentException ("Время или интервал не может быть отрицательным");
        }
    }

    public Task(String title, Date time) throws IllegalArgumentException {
        this.title = title;
        this.start = time;
        this.end = time;
    }

    /**
     *
     * @param title - name of task
     * @param start - date of task
     * @param end - end date of task
     * @param interval -
     * @throws IllegalArgumentException
     */
    public Task(String title, int start, int end, int interval) throws IllegalArgumentException {
        this.title = title;
        this.start = new Date(start*1000);
        this.end = new Date(end*1000);
        this.interval = interval*1000;
        if (start < 0 || end <0 || interval <0)
        {
            throw new IllegalArgumentException ("Время или интервал не может быть отрицательным");
        }
        else if (start > end) {
            throw new IllegalArgumentException ("Время начала не может быть позже время окончания");
        }
    }
    public Task(String title, Date start, Date end, int interval) throws IllegalArgumentException {
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval*1000;
    }

    public int getInterval() {
        return interval;
    }
    /**
     * метод считывания названия задачи
     * return title название задачи
     */
    public String getTitle(){
        return title;
    }

    /**
     * метод установлпения названия задачи
     * @param title название, которое надо поменять
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * метод считывания состояния задачи
     * @return active true - если задача активна, falls - если не активна
     */
    public boolean isActive() {
        return active;
    }

    /**
     * метод установлпения состояния задачи
     * @param active true - если задача активна, falls - если не активна
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * метод возвращает время не повторяюшейся задачи
     * @return start если задача не повторяется - возвращает start - при том, что start = time (см конструктор)
     * @return start если задача повторяется, при этом start = start (см конструктор)
     */
    public int getTime() {
        return (int)this.start.getTime();
    }

    /**
     * метод изменяет повторяющейся задачу на не повторяющейся
     * @param time = start и = end, если задача не повторяется, то start = end
     * interval = 0 т.к. задача не повторяется
     */
    public void setTime(int time) throws IllegalArgumentException {
        this.start = new Date(time*1000);
        this.end = new Date(time*1000);
        this.interval = 0;
        if (time < 0 )
        {
            throw new IllegalArgumentException ("Время или интервал не может быть отрицательным");
        }
    }

    public void setTime(Date time) throws IllegalArgumentException {
        this.start = time;
        this.end = time;
        this.interval = 0;
    }

    /**
     * метод возвращает время начала повторяюшейся задачи
     * @return start если задача повторяется, при этом start = start (см конструктор)
     * @return start если задача не повторяется - возвращает start - при том, что start = time (см конструктор)
     */
    public int getStartTime() {
        return (int)this.start.getTime();
    }

    /**
     * метод возвращает время окончания повторяюшейся задачи
     * @return end если задача повторяется, при этом end = end (см конструктор)
     * @return end если задача не повторяется - возвращает end - при том, что end = time (см конструктор)
     */
    public int getEndTime() {
        return (int)this.end.getTime();
    }

    /**
     * метод возвращает интервал времени повторяюшейся задачи
     * @return interval если задача повторяется, interval = interval
     * @return interval если задача не повторяется, interval = 0 по умолчанию, возвращает 0
     */
    public int getRepeatInterval() {
        return interval;
    }

    /**
     * метод изменяет не повторяющейся задачу на повторяющейся
     * @param start = start
     * @param end = end
     * @param interval = interval
     */
    public void setTime(int start, int end, int interval) throws IllegalArgumentException {
        this.start = new Date(start*1000);
        this.end = new Date(end*1000);
        this.interval = interval;
        if (start < 0 || end < 0 || interval <0) {
            throw new IllegalArgumentException("Время или интервал не может быть отрицательным");
        }
        else if (start > end) {
            throw new IllegalArgumentException ("Время начала не может быть позже время окончания");
        }
    }

    /**
     * метод проверяет повторяется ли задача
     * @return true если interval>0
     * @return falls если interval<0
     */
    public boolean isRepeated() {
        return this.interval>0;
    }

    /**
     * метод возвращает время следующего выполнения задачи после указания время
     * @param current указанное время
     * @return -1 если задача не активная
     * @return start + (interval*i) если задача активна, и указанное время current >= start и в сумме с start не больше end
     * если задача не повторяется, то end = start, и условие end > start + current не выполняется
     * @return start если задача активна, и указанное время current < start
     * @return -1 если задача не выполняется в указанное время current или была выполненна
     */
    public Date nextTimeAfter(Date current) throws IllegalArgumentException {
        Date date = new Date(-1);
        if (isActive()) {
            if (!isRepeated()) {
                if (this.start.after(current))
                    return this.start;
                else
                    return new Date(-1);
            } else {
                for (long i = this.start.getTime(); i <= this.end.getTime(); i = i + this.interval) {
                    date.setTime(i);
                    if (date.after(current) || date.equals(current)) {
                        return date;
                    } else date.setTime(-1);
                }
            }
        }
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (interval != task.interval) return false;
        if (isActive() != task.isActive()) return false;
        if (!getTitle().equals(task.getTitle())) return false;
        if (!start.equals(task.start)) return false;
        return end.equals(task.end);

    }

    @Override
    public int hashCode() {
        int result = getTitle().hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + interval;
        result = 31 * result + (isActive() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        SimpleDateFormat date = new SimpleDateFormat("[YYYY-MM-dd hh:mm:ss.SSS]");

        String text = "";
        text += "\"" + this.title + "\"";
        if (isRepeated()) {
            text += " from " + date.format(this.start);
            text += " to " + date.format(this.end);
            text += " every [" + reInterval(interval) + "]";
        }
        else {
            text += " at " + date.format(this.start);
        }
        text +=  (this.active ? " inactive" : "");

        return text;
    }

    public Task clone() throws CloneNotSupportedException {
        Task task;
        if (this.interval ==0) {
            task = new Task(this.getTitle(), (Date)this.start.clone());
        }
        else
            task = new Task(this.getTitle(), (Date)this.start.clone(), (Date)this.end.clone(), this.interval);
        return task;
    }

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

